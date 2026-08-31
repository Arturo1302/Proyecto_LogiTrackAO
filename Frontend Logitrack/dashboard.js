// ===== Navegación =====
function mostrarSeccion(id, tabEl) {
    document.querySelectorAll('.seccion').forEach(s => s.style.display = 'none');
    document.getElementById(id).style.display = 'block';
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    tabEl.classList.add('active');
    if (id === 'bodegas') cargarBodegas();
    if (id === 'productos') { cargarCategoriasSelect('productoCategoria'); cargarProductos(); }
    if (id === 'categorias') cargarCategorias();
    if (id === 'usuarios') { cargarRolesSelect(); cargarUsuarios(); }
    if (id === 'stock') { cargarSelectsStock(); cargarStock(); }
    if (id === 'movimientos') { cargarSelectsMovimiento(); cargarMovimientos(); }
    if (id === 'auditorias') cargarAuditorias();
    if (id === 'reporte') cargarReporte();
}

// ===== Modal genérico =====
function abrirModal(titulo, campos, onGuardar) {
    document.getElementById('modalTitle').textContent = titulo;
    document.getElementById('modalFields').innerHTML = campos.map(c => {
        if (c.type === 'select') {
            return `<label>${c.label}</label><select id="modal_${c.id}">${c.options.map(o =>
                `<option value="${o.value}" ${String(o.value) === String(c.value) ? 'selected' : ''}>${o.label}</option>`).join('')}</select>`;
        }
        return `<label>${c.label}</label><input type="${c.type}" id="modal_${c.id}" value="${c.value ?? ''}">`;
    }).join('');
    document.getElementById('modalOverlay').style.display = 'flex';

    const oldBtn = document.getElementById('modalSave');
    const newBtn = oldBtn.cloneNode(true);
    oldBtn.parentNode.replaceChild(newBtn, oldBtn);
    newBtn.addEventListener('click', async () => {
        const valores = {};
        campos.forEach(c => valores[c.id] = document.getElementById(`modal_${c.id}`).value);
        await onGuardar(valores);
        cerrarModal();
    });
}
function cerrarModal() { document.getElementById('modalOverlay').style.display = 'none'; }
document.getElementById('modalCancel').addEventListener('click', cerrarModal);
document.getElementById('modalOverlay').addEventListener('click', (e) => {
    if (e.target.id === 'modalOverlay') cerrarModal();
});

// ===== Bodegas =====
async function cargarBodegas() {
    const [bodegas, usuarios] = await Promise.all([apiFetch('/bodegas'), apiFetch('/usuarios')]);
    document.querySelector('#tablaBodegas tbody').innerHTML = bodegas.map(b => `
        <tr>
            <td>${b.id}</td><td>${b.nombre}</td><td>${b.ubicacion}</td><td>${b.capacidad}</td>
            <td>${b.encargadoUsername ?? '—'}</td>
            <td>
                <button class="btn-edit" onclick='editarBodega(${JSON.stringify(b)}, ${JSON.stringify(usuarios)})'>Editar</button>
                <button class="btn-delete" onclick="eliminarBodega(${b.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

function editarBodega(b, usuarios) {
    abrirModal('Editar bodega', [
        { id: 'nombre', label: 'Nombre', type: 'text', value: b.nombre },
        { id: 'ubicacion', label: 'Ubicación', type: 'text', value: b.ubicacion },
        { id: 'capacidad', label: 'Capacidad', type: 'number', value: b.capacidad },
        { id: 'encargadoId', label: 'Encargado', type: 'select', value: '',
          options: [{ value: '', label: 'Sin encargado' }, ...usuarios.map(u => ({ value: u.id, label: u.username }))] }
    ], async (v) => {
        await apiFetch(`/bodegas/${b.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                nombre: v.nombre, ubicacion: v.ubicacion,
                capacidad: parseInt(v.capacidad),
                encargadoId: v.encargadoId ? parseInt(v.encargadoId) : null
            })
        });
        cargarBodegas();
    });
}

async function eliminarBodega(id) {
    if (!confirm('¿Eliminar esta bodega?')) return;
    await apiFetch(`/bodegas/${id}`, { method: 'DELETE' });
    cargarBodegas();
}

document.getElementById('formBodega').addEventListener('submit', async (e) => {
    e.preventDefault();
    await apiFetch('/bodegas', {
        method: 'POST',
        body: JSON.stringify({
            nombre: document.getElementById('bodegaNombre').value,
            ubicacion: document.getElementById('bodegaUbicacion').value,
            capacidad: parseInt(document.getElementById('bodegaCapacidad').value),
            encargadoId: null
        })
    });
    e.target.reset();
    cargarBodegas();
});

// ===== Productos =====
async function cargarCategoriasSelect(selectId) {
    const categorias = await apiFetch('/categorias');
    document.getElementById(selectId).innerHTML = categorias.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('');
}

async function cargarProductos() {
    const productos = await apiFetch('/productos');
    document.querySelector('#tablaProductos tbody').innerHTML = productos.map(p => `
        <tr>
            <td>${p.id}</td><td>${p.nombre}</td><td>${p.categoriaNombre}</td><td>$${p.precio}</td>
            <td>
                <button class="btn-edit" onclick='editarProducto(${JSON.stringify(p)})'>Editar</button>
                <button class="btn-delete" onclick="eliminarProducto(${p.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function editarProducto(p) {
    const categorias = await apiFetch('/categorias');
    abrirModal('Editar producto', [
        { id: 'nombre', label: 'Nombre', type: 'text', value: p.nombre },
        { id: 'categoriaId', label: 'Categoría', type: 'select',
          value: categorias.find(c => c.nombre === p.categoriaNombre)?.id ?? '',
          options: categorias.map(c => ({ value: c.id, label: c.nombre })) },
        { id: 'precio', label: 'Precio', type: 'number', value: p.precio }
    ], async (v) => {
        await apiFetch(`/productos/${p.id}`, {
            method: 'PUT',
            body: JSON.stringify({ nombre: v.nombre, categoriaId: parseInt(v.categoriaId), precio: parseFloat(v.precio) })
        });
        cargarProductos();
    });
}

async function eliminarProducto(id) {
    if (!confirm('¿Eliminar este producto?')) return;
    await apiFetch(`/productos/${id}`, { method: 'DELETE' });
    cargarProductos();
}

document.getElementById('formProducto').addEventListener('submit', async (e) => {
    e.preventDefault();
    await apiFetch('/productos', {
        method: 'POST',
        body: JSON.stringify({
            nombre: document.getElementById('productoNombre').value,
            categoriaId: parseInt(document.getElementById('productoCategoria').value),
            precio: parseFloat(document.getElementById('productoPrecio').value)
        })
    });
    e.target.reset();
    cargarProductos();
});

// ===== Categorías =====
async function cargarCategorias() {
    const categorias = await apiFetch('/categorias');
    document.querySelector('#tablaCategorias tbody').innerHTML = categorias.map(c => `
        <tr>
            <td>${c.id}</td><td>${c.nombre}</td><td>${c.descripcion ?? ''}</td>
            <td>
                <button class="btn-edit" onclick='editarCategoria(${JSON.stringify(c)})'>Editar</button>
                <button class="btn-delete" onclick="eliminarCategoria(${c.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

function editarCategoria(c) {
    abrirModal('Editar categoría', [
        { id: 'nombre', label: 'Nombre', type: 'text', value: c.nombre },
        { id: 'descripcion', label: 'Descripción', type: 'text', value: c.descripcion ?? '' }
    ], async (v) => {
        await apiFetch(`/categorias/${c.id}`, {
            method: 'PUT',
            body: JSON.stringify({ nombre: v.nombre, descripcion: v.descripcion })
        });
        cargarCategorias();
    });
}

async function eliminarCategoria(id) {
    if (!confirm('¿Eliminar esta categoría?')) return;
    await apiFetch(`/categorias/${id}`, { method: 'DELETE' });
    cargarCategorias();
}

document.getElementById('formCategoria').addEventListener('submit', async (e) => {
    e.preventDefault();
    await apiFetch('/categorias', {
        method: 'POST',
        body: JSON.stringify({
            nombre: document.getElementById('categoriaNombre').value,
            descripcion: document.getElementById('categoriaDescripcion').value || null
        })
    });
    e.target.reset();
    cargarCategorias();
});

// ===== Usuarios =====
async function cargarRolesSelect() {
    const roles = await apiFetch('/roles');
    document.getElementById('usuarioRol').innerHTML = roles.map(r => `<option value="${r.id}">${r.nombre}</option>`).join('');
}

async function cargarUsuarios() {
    const usuarios = await apiFetch('/usuarios');
    document.querySelector('#tablaUsuarios tbody').innerHTML = usuarios.map(u => `
        <tr>
            <td>${u.id}</td><td>${u.username}</td><td>${u.email}</td><td>${u.rolNombre}</td>
            <td>
                <button class="btn-edit" onclick='editarUsuario(${JSON.stringify(u)})'>Editar</button>
                <button class="btn-delete" onclick="eliminarUsuario(${u.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function editarUsuario(u) {
    const roles = await apiFetch('/roles');
    abrirModal('Editar usuario', [
        { id: 'username', label: 'Username', type: 'text', value: u.username },
        { id: 'email', label: 'Email', type: 'text', value: u.email },
        { id: 'password', label: 'Nueva contraseña', type: 'password', value: '' },
        { id: 'rolId', label: 'Rol', type: 'select',
          value: roles.find(r => r.nombre === u.rolNombre)?.id ?? '',
          options: roles.map(r => ({ value: r.id, label: r.nombre })) }
    ], async (v) => {
        await apiFetch(`/usuarios/${u.id}`, {
            method: 'PUT',
            body: JSON.stringify({ username: v.username, email: v.email, password: v.password, rolId: parseInt(v.rolId) })
        });
        cargarUsuarios();
    });
}

async function eliminarUsuario(id) {
    if (!confirm('¿Eliminar este usuario?')) return;
    await apiFetch(`/usuarios/${id}`, { method: 'DELETE' });
    cargarUsuarios();
}

document.getElementById('formUsuario').addEventListener('submit', async (e) => {
    e.preventDefault();
    await apiFetch('/usuarios', {
        method: 'POST',
        body: JSON.stringify({
            username: document.getElementById('usuarioUsername').value,
            email: document.getElementById('usuarioEmail').value,
            password: document.getElementById('usuarioPassword').value,
            rolId: parseInt(document.getElementById('usuarioRol').value)
        })
    });
    e.target.reset();
    cargarUsuarios();
});

// ===== Stock por bodega =====
async function cargarSelectsStock() {
    const [bodegas, productos] = await Promise.all([apiFetch('/bodegas'), apiFetch('/productos')]);
    document.getElementById('stockBodega').innerHTML = bodegas.map(b => `<option value="${b.id}">${b.nombre}</option>`).join('');
    document.getElementById('stockProducto').innerHTML = productos.map(p => `<option value="${p.id}">${p.nombre}</option>`).join('');
}

function renderStock(registros) {
    document.querySelector('#tablaStock tbody').innerHTML = registros.map(s => `
        <tr>
            <td>${s.bodegaNombre}</td><td>${s.productoNombre}</td><td>${s.stock}</td>
            <td>
                <button class="btn-edit" onclick="editarStock(${s.bodegaId}, ${s.productoId}, ${s.stock})">Editar</button>
                <button class="btn-delete" onclick="eliminarStock(${s.bodegaId}, ${s.productoId})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function cargarStock() {
    renderStock(await apiFetch('/bodega-producto'));
}

function editarStock(bodegaId, productoId, stockActual) {
    abrirModal('Actualizar stock', [
        { id: 'stock', label: 'Nuevo stock', type: 'number', value: stockActual }
    ], async (v) => {
        await apiFetch(`/bodega-producto/${bodegaId}/${productoId}`, {
            method: 'PUT',
            body: JSON.stringify({ stock: parseInt(v.stock) })
        });
        cargarStock();
    });
}

async function eliminarStock(bodegaId, productoId) {
    if (!confirm('¿Eliminar este registro de stock?')) return;
    await apiFetch(`/bodega-producto/${bodegaId}/${productoId}`, { method: 'DELETE' });
    cargarStock();
}

document.getElementById('formStock').addEventListener('submit', async (e) => {
    e.preventDefault();
    await apiFetch('/bodega-producto', {
        method: 'POST',
        body: JSON.stringify({
            bodegaId: parseInt(document.getElementById('stockBodega').value),
            productoId: parseInt(document.getElementById('stockProducto').value),
            stock: parseInt(document.getElementById('stockCantidad').value)
        })
    });
    e.target.reset();
    cargarStock();
});

document.getElementById('btnFiltrarStock').addEventListener('click', async () => {
    const umbral = document.getElementById('umbralStock').value || 10;
    renderStock(await apiFetch(`/bodega-producto/stock-bajo?umbral=${umbral}`));
});
document.getElementById('btnVerTodoStock').addEventListener('click', cargarStock);

// ===== Movimientos =====
let productosCache = [];

async function cargarSelectsMovimiento() {
    const [usuarios, bodegas, productos] = await Promise.all([
        apiFetch('/usuarios'), apiFetch('/bodegas'), apiFetch('/productos')
    ]);
    productosCache = productos;
    document.getElementById('movUsuario').innerHTML =
        '<option value="">Usuario responsable</option>' + usuarios.map(u => `<option value="${u.id}">${u.username}</option>`).join('');
    const bodegaOptions = bodegas.map(b => `<option value="${b.id}">${b.nombre}</option>`).join('');
    document.getElementById('movBodegaOrigen').innerHTML = '<option value="">Bodega origen</option>' + bodegaOptions;
    document.getElementById('movBodegaDestino').innerHTML = '<option value="">Bodega destino</option>' + bodegaOptions;
    if (document.querySelectorAll('.detalle-item').length === 0) agregarItemDetalle();
}

function opcionesProductos() {
    return productosCache.map(p => `<option value="${p.id}">${p.nombre}</option>`).join('');
}

function agregarItemDetalle() {
    const div = document.createElement('div');
    div.className = 'detalle-item';
    div.innerHTML = `
        <select class="detalle-producto">${opcionesProductos()}</select>
        <input type="number" class="detalle-cantidad" placeholder="Cantidad" min="1" required>
        <button type="button" class="btn-remove" onclick="this.parentElement.remove()">×</button>`;
    document.getElementById('detalleItems').appendChild(div);
}
document.getElementById('btnAgregarItem').addEventListener('click', agregarItemDetalle);

document.getElementById('movTipo').addEventListener('change', (e) => {
    const tipo = e.target.value;
    document.getElementById('movBodegaOrigen').style.display = (tipo === 'SALIDA' || tipo === 'TRANSFERENCIA') ? 'block' : 'none';
    document.getElementById('movBodegaDestino').style.display = (tipo === 'ENTRADA' || tipo === 'TRANSFERENCIA') ? 'block' : 'none';
});

function renderMovimientos(movs) {
    document.querySelector('#tablaMovimientos tbody').innerHTML = movs.map(m => `
        <tr>
            <td>${m.id}</td><td>${m.fechaHora ?? ''}</td><td>${m.tipo}</td><td>${m.usuarioUsername}</td>
            <td>${m.bodegaOrigenNombre ?? '—'}</td><td>${m.bodegaDestinoNombre ?? '—'}</td>
        </tr>`).join('');
}

async function cargarMovimientos() {
    renderMovimientos(await apiFetch('/movimientos'));
}

document.getElementById('formMovimiento').addEventListener('submit', async (e) => {
    e.preventDefault();
    const errorEl = document.getElementById('movError');
    errorEl.textContent = '';
    const detalles = [...document.querySelectorAll('.detalle-item')].map(item => ({
        productoId: parseInt(item.querySelector('.detalle-producto').value),
        cantidad: parseInt(item.querySelector('.detalle-cantidad').value)
    }));
    const origenVal = document.getElementById('movBodegaOrigen').value;
    const destinoVal = document.getElementById('movBodegaDestino').value;
    try {
        await apiFetch('/movimientos', {
            method: 'POST',
            body: JSON.stringify({
                tipo: document.getElementById('movTipo').value,
                usuarioId: parseInt(document.getElementById('movUsuario').value),
                bodegaOrigenId: origenVal ? parseInt(origenVal) : null,
                bodegaDestinoId: destinoVal ? parseInt(destinoVal) : null,
                detalles
            })
        });
        e.target.reset();
        document.getElementById('detalleItems').innerHTML = '';
        agregarItemDetalle();
        cargarMovimientos();
    } catch (err) {
        errorEl.textContent = 'No se pudo registrar el movimiento. Revisa el stock disponible.';
    }
});

document.getElementById('btnFiltrarMov').addEventListener('click', async () => {
    const inicio = document.getElementById('movFechaInicio').value;
    const fin = document.getElementById('movFechaFin').value;
    if (!inicio || !fin) return alert('Selecciona ambas fechas');
    renderMovimientos(await apiFetch(`/movimientos/por-fecha?inicio=${inicio}&fin=${fin}`));
});
document.getElementById('btnVerTodoMov').addEventListener('click', cargarMovimientos);

// ===== Auditorías =====
function renderAuditorias(registros) {
    document.querySelector('#tablaAuditorias tbody').innerHTML = registros.map(a => `
        <tr>
            <td>${a.id}</td><td>${a.operacion}</td><td>${a.entidad}</td><td>${a.entidadId}</td>
            <td>${a.fechaHora ?? ''}</td><td>${a.usuario}</td>
        </tr>`).join('');
}

async function cargarAuditorias() {
    renderAuditorias(await apiFetch('/auditorias'));
}

document.getElementById('btnFiltrarAudit').addEventListener('click', async () => {
    const usuario = document.getElementById('auditUsuario').value;
    const operacion = document.getElementById('auditOperacion').value;
    if (usuario) renderAuditorias(await apiFetch(`/auditorias/por-usuario?usuario=${encodeURIComponent(usuario)}`));
    else if (operacion) renderAuditorias(await apiFetch(`/auditorias/por-operacion?operacion=${operacion}`));
    else cargarAuditorias();
});
document.getElementById('btnVerTodoAudit').addEventListener('click', cargarAuditorias);

// ===== Reporte =====
async function cargarReporte() {
    const reporte = await apiFetch('/reportes/general');
    const bodegasHtml = reporte.stockPorBodega.map(s => `<li><span>${s.bodegaNombre}</span><strong>${s.stockTotal}</strong></li>`).join('');
    const productosHtml = reporte.productosMasMovidos.map(p => `<li><span>${p.productoNombre}</span><strong>${p.cantidadTotal}</strong></li>`).join('');
    document.getElementById('reporteContenido').innerHTML = `
        <div class="reporte-card"><h3>Stock por bodega</h3><ul>${bodegasHtml || '<li>Sin datos</li>'}</ul></div>
        <div class="reporte-card"><h3>Productos más movidos</h3><ul>${productosHtml || '<li>Sin datos</li>'}</ul></div>`;
}
document.getElementById('btnActualizarReporte').addEventListener('click', cargarReporte);

// Carga inicial
cargarBodegas();