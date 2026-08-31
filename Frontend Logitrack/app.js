

const API_BASE = 'http://localhost:8080';

function getToken() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
    }
    return token;
}

async function apiFetch(path, options = {}) {
    const token = getToken();
    const response = await fetch(API_BASE + path, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
            ...options.headers
        }
    });

    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem('token');
        window.location.href = 'index.html';
        return;
    }

    if (response.status === 204) return null;
    return response.json();
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}