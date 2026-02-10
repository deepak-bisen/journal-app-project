// Update this URL to your production backend URL
const API_BASE = "https://journal-app-project-production.up.railway.app";
let currentUser = null;
const state = { entries: [], weather: null, greeting: "Hello", adminUsers: [] };

async function apiRequest(endpoint, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    if (currentUser && currentUser.authHeader) headers['Authorization'] = currentUser.authHeader;
    
    // Ensure no double slashes if endpoint starts with /
    const url = endpoint.startsWith('/') ? `${API_BASE}${endpoint}` : `${API_BASE}/${endpoint}`;
    
    try {
        const response = await fetch(url, { ...options, headers });

        if (options.quiet && !response.ok) return null;

        if (response.status === 401) { 
            showToast("Session expired", "error"); 
            logout(); 
            return null; 
        }
        
        if (!response.ok) { 
            const error = await response.text(); 
            throw new Error(error || 'Request failed'); 
        }
        
        if (response.status === 204) return true;
        const contentType = response.headers.get("content-type");
        return (contentType && contentType.includes("application/json")) ? await response.json() : await response.text();
    } catch (err) {
        if (!options.quiet) {
            console.error("API Error:", err);
            showToast("Connection Error: Check console for details", "error");
        }
        return null;
    }
}
const router = {
    home: () => currentUser ? router.dashboard() : renderHome(),
    login: () => renderLogin(),
    signup: () => renderSignup(),
    admin: () => renderAdminDashboard(),
    dashboard: async () => {
        const root = document.getElementById('app-root');
        root.innerHTML = `<div class="flex justify-center py-20"><div class="w-6 h-6 border-2 border-slate-700 border-t-indigo-600 rounded-full animate-spin"></div></div>`;
        const greetData = await apiRequest('/user/greeting');
        if (greetData) state.greeting = greetData;
        const entries = await apiRequest('/journal/getAll');
        state.entries = Array.isArray(entries) ? entries : [];
        renderDashboard();
    }
};

function renderHome() {
    updateNav();
    document.getElementById('app-root').innerHTML = `
                <div class="flex flex-col md:flex-row items-center justify-between py-12 gap-12 animate-fade-in">
                    <div class="md:w-1/2 space-y-6 text-center md:text-left">
                        <h1 class="text-5xl md:text-7xl font-extrabold leading-tight">Write your thoughts, <span class="text-indigo-500 font-black">understand</span> yourself.</h1>
                        <p class="text-slate-400 text-lg max-w-lg mx-auto md:mx-0">A secure, minimal journal app features sentiment analysis, weather insights, and daily reflections.</p>
                        <div class="flex flex-col sm:flex-row items-center gap-4 justify-center md:justify-start pt-4">
                            <button onclick="router.signup()" class="bg-indigo-600 hover:bg-indigo-700 text-white px-8 py-4 rounded-xl font-bold transition-all w-full sm:w-auto shadow-lg shadow-indigo-500/30">Get Started Free</button>
                            <button onclick="router.login()" class="bg-slate-800 hover:bg-slate-700 text-white px-8 py-4 rounded-xl font-bold transition-all w-full sm:w-auto border border-slate-700">Sign In</button>
                        </div>
                    </div>
                    <div class="md:w-1/2 grid grid-cols-2 gap-4">
                        <div class="glass-card p-6 rounded-3xl mt-8 space-y-3"><i class="fas fa-chart-line text-2xl text-green-400"></i><h3 class="font-bold">Sentiment Tracking</h3><p class="text-sm text-slate-400">Understand your mood patterns.</p></div>
                        <div class="glass-card p-6 rounded-3xl space-y-3"><i class="fas fa-cloud-sun text-2xl text-blue-400"></i><h3 class="font-bold">Weather Context</h3><p class="text-sm text-slate-400">See how environment affects your mindset.</p></div>
                        <div class="glass-card p-6 rounded-3xl space-y-3"><i class="fas fa-lock text-2xl text-purple-400"></i><h3 class="font-bold">Encrypted & Secure</h3><p class="text-sm text-slate-400">Private thoughts stay private.</p></div>
                        <div class="glass-card p-6 rounded-3xl -mt space-y-3"><i class="fas fa-bell text-2xl text-yellow-400"></i><h3 class="font-bold">Weekly Insights</h3><p class="text-sm text-slate-400">Email reports based on entries.</p></div>
                    </div>
                </div>`;
}

function renderLogin() {
    updateNav();
    document.getElementById('app-root').innerHTML = `
                <div class="max-w-md mx-auto py-12 animate-fade-in">
                    <div class="glass-card p-8 rounded-3xl space-y-6">
                        <div class="text-center"><h2 class="text-3xl font-bold">Welcome Back</h2><p class="text-slate-400 mt-2">Sign in to your account</p></div>
                        <form id="login-form" class="space-y-4">
                            <div><label class="block text-sm font-medium mb-2">Username</label><input type="text" name="username" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"></div>
                            <div><label class="block text-sm font-medium mb-2">Password</label><input type="password" name="password" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"></div>
                            <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl transition-all shadow-lg shadow-indigo-500/30">Log In</button>
                        </form>
                        <p class="text-center text-sm text-slate-400">Don't have an account? <a href="#" onclick="router.signup()" class="text-indigo-400 hover:underline">Sign up</a></p>
                    </div>
                </div>`;

    document.getElementById('login-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const user = fd.get('username'), pass = fd.get('password');
        const authHeader = 'Basic ' + btoa(user + ':' + pass);

        // 1. Check valid login via greeting
        const test = await apiRequest('/user/greeting', { headers: { 'Authorization': authHeader } });

        if (test) {
            // 2. Check if user is an admin by attempting to access admin endpoint
            const adminCheck = await apiRequest('/admin/all-users', {
                headers: { 'Authorization': authHeader },
                quiet: true
            });

            currentUser = {
                username: user,
                authHeader,
                isAdmin: adminCheck !== null
            };

            localStorage.setItem('journal_user', JSON.stringify(currentUser));
            showToast(`Welcome back, ${user}!`);
            router.dashboard();
        } else { showToast("Invalid credentials", "error"); }
    });
}

async function renderAdminDashboard() {
    if (!currentUser || !currentUser.isAdmin) { router.home(); return; }
    updateNav();

    const root = document.getElementById('app-root');
    root.innerHTML = `<div class="flex justify-center py-20"><div class="w-6 h-6 border-2 border-slate-700 border-t-indigo-600 rounded-full animate-spin"></div></div>`;

    const users = await apiRequest('/admin/all-users');
    state.adminUsers = Array.isArray(users) ? users : [];

    root.innerHTML = `
                <div class="animate-fade-in space-y-8">
                    <header class="flex justify-between items-center">
                        <div>
                            <h2 class="text-3xl font-extrabold text-white">Admin Dashboard</h2>
                            <p class="text-slate-400">System-wide user management and maintenance.</p>
                        </div>
                        <div class="flex space-x-3">
                             <button onclick="clearCache()" class="bg-slate-800 hover:bg-slate-700 text-white px-5 py-2.5 rounded-xl font-bold border border-slate-700 flex items-center space-x-2">
                                <i class="fas fa-sync text-xs"></i><span>Clear Cache</span>
                            </button>
                            <button onclick="openCreateAdminModal()" class="bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2.5 rounded-xl font-bold flex items-center space-x-2">
                                <i class="fas fa-user-shield text-xs"></i><span>Add Admin</span>
                            </button>
                        </div>
                    </header>

                    <div class="glass-card rounded-2xl overflow-hidden">
                        <table class="w-full text-left">
                            <thead class="bg-slate-800/50 border-b border-slate-700">
                                <tr>
                                    <th class="px-6 py-4 text-xs font-bold uppercase text-slate-400 tracking-wider">Username</th>
                                    <th class="px-6 py-4 text-xs font-bold uppercase text-slate-400 tracking-wider">Email</th>
                                    <th class="px-6 py-4 text-xs font-bold uppercase text-slate-400 tracking-wider">Role</th>
                                    <th class="px-6 py-4 text-xs font-bold uppercase text-slate-400 tracking-wider text-right">Entries</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-800">
                                ${state.adminUsers.map(u => `
                                    <tr class="hover:bg-slate-800/30 transition-colors">
                                        <td class="px-6 py-4 font-medium">${u.userName}</td>
                                        <td class="px-6 py-4 text-slate-400">${u.email || 'N/A'}</td>
                                        <td class="px-6 py-4">
                                            <span class="px-2 py-1 rounded-md text-[10px] font-black uppercase ${u.role === 'ADMIN' ? 'bg-indigo-500/20 text-indigo-400 border border-indigo-500/30' : 'bg-slate-700 text-slate-300'}">
                                                ${u.role}
                                            </span>
                                        </td>
                                        <td class="px-6 py-4 text-right text-slate-500">${u.journalEntries ? u.journalEntries.length : 0}</td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                    </div>
                </div>`;
}

async function clearCache() {
    if (await apiRequest('/admin/clear-app-cache')) {
        showToast("System cache cleared successfully!");
    }
}

function openCreateAdminModal() {
    const modal = document.getElementById('modal-container'), content = document.getElementById('modal-content');
    modal.classList.remove('hidden');
    content.innerHTML = `
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-bold">New Admin User</h3>
                    <button onclick="closeModal()" class="text-slate-400 hover:text-white"><i class="fas fa-times"></i></button>
                </div>
                <form id="admin-create-form" class="space-y-4">
                    <div><label class="block text-sm font-medium mb-2">Username</label><input type="text" name="userName" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"></div>
                    <div><label class="block text-sm font-medium mb-2">Email Address</label><input type="email" name="email" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"></div>
                    <div><label class="block text-sm font-medium mb-2">Password</label><input type="password" name="password" minlength="6" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"></div>
                    <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl transition-all mt-4 shadow-lg">Create Admin User</button>
                </form>`;

    document.getElementById('admin-create-form').onsubmit = async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = {
            userName: fd.get('userName'),
            email: fd.get('email'),
            password: fd.get('password')
        };
        if (await apiRequest('/admin/create-admin-user', { method: 'POST', body: JSON.stringify(payload) })) {
            showToast("Admin account created!");
            closeModal();
            router.admin();
        }
    };
}

function renderSignup() {
    updateNav();
    document.getElementById('app-root').innerHTML = `
                <div class="max-w-md mx-auto py-12 animate-fade-in">
                    <div class="glass-card p-8 rounded-3xl space-y-6">
                        <div class="text-center"><h2 class="text-3xl font-bold">Create Account</h2><p class="text-slate-400 mt-2">Join ZenJournal today</p></div>
                        <form id="signup-form" class="space-y-4">
                            <div><label class="block text-sm font-medium mb-2">Username</label><input type="text" name="userName" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"></div>
                            <div><label class="block text-sm font-medium mb-2">Email Address</label><input type="email" name="email" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"></div>
                            <div><label class="block text-sm font-medium mb-2">Password</label><input type="password" name="password" minlength="6" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"></div>
                            <div class="flex items-center space-x-2 py-2">
                                <input type="checkbox" name="sentimentAnalysis" id="sa" class="rounded bg-slate-950 border-slate-700 text-indigo-600 focus:ring-indigo-500">
                                <label for="sa" class="text-sm text-slate-300">Enable Weekly Sentiment Analysis via Email</label>
                            </div>
                            <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl transition-all shadow-lg shadow-indigo-500/30">Register Now</button>
                        </form>
                        <p class="text-center text-sm text-slate-400">Already have an account? <a href="#" onclick="router.login()" class="text-indigo-400 hover:underline">Log in</a></p>
                    </div>
                </div>`;

    document.getElementById('signup-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = {
            userName: fd.get('userName'),
            email: fd.get('email'),
            password: fd.get('password'),
            sentimentAnalysis: fd.get('sentimentAnalysis') === 'on'
        };
        const result = await apiRequest('/public/create-user', { method: 'POST', body: JSON.stringify(payload) });
        if (result) { showToast("Account created! Please log in."); router.login(); }
    });
}

function renderDashboard() {
    updateNav();
    const root = document.getElementById('app-root');
    const entriesHtml = state.entries.length === 0
        ? `<div class="text-center py-20 bg-slate-800/10 rounded-3xl border border-dashed border-slate-700"><i class="fas fa-feather-alt text-4xl text-slate-600 mb-4"></i><p class="text-slate-400 font-medium">Your journal is empty.</p></div>`
        : `<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    ${state.entries.map(e => `
                        <div class="glass-card p-6 rounded-2xl flex flex-col justify-between group animate-fade-in hover:border-indigo-500/30 transition-all">
                            <div>
                                <div class="flex justify-between items-start mb-4">
                                    <span class="text-xs font-semibold uppercase tracking-wider text-slate-500">${new Date(e.date).toLocaleDateString()}</span>
                                    ${e.sentiment ? `<span class="sentiment-${e.sentiment.toLowerCase()} text-[10px] px-2 py-1 rounded-full font-bold uppercase">${e.sentiment}</span>` : ''}
                                </div>
                                <h3 class="text-xl font-bold mb-2 group-hover:text-indigo-400 transition-colors">${e.title}</h3>
                                <p class="text-slate-400 text-sm line-clamp-3 mb-6">${e.content || 'No content.'}</p>
                            </div>
                            <div class="flex items-center justify-end space-x-3 border-t border-slate-800 pt-4">
                                <button onclick="openEditModal('${e.uuid}')" class="text-slate-400 hover:text-white p-2 transition-colors"><i class="fas fa-edit"></i></button>
                                <button onclick="deleteEntry('${e.uuid}')" class="text-slate-400 hover:text-red-400 p-2 transition-colors"><i class="fas fa-trash"></i></button>
                            </div>
                        </div>`).join('')}</div>`;

    root.innerHTML = `
                <div class="animate-fade-in space-y-8">
                    <header class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                        <div><h2 class="text-3xl font-extrabold text-white">${state.greeting}</h2><p class="text-slate-400">Manage your reflections.</p></div>
                        <button onclick="openCreateModal()" class="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-bold transition-all shadow-lg shadow-indigo-500/30 flex items-center space-x-2"><i class="fas fa-plus text-xs"></i><span>New Entry</span></button>
                    </header>
                    ${entriesHtml}
                </div>`;
}

function updateNav() {
    const actions = document.getElementById('nav-actions');
    const homeBtn = `<button onclick="router.home()" class="text-slate-400 hover:text-white text-sm font-bold px-4 py-2 transition-colors flex items-center space-x-2"><i class="fas fa-home text-xs"></i><span>Home</span></button>`;

    if (currentUser) {
        const adminBtn = currentUser.isAdmin
            ? `<button onclick="router.admin()" class="text-indigo-400 hover:text-indigo-300 text-sm font-bold px-4 py-2 transition-colors flex items-center space-x-2 border border-indigo-500/30 rounded-lg bg-indigo-500/5"><i class="fas fa-shield-alt text-xs"></i><span>Admin Panel</span></button>`
            : '';

        actions.innerHTML = `
                    <div class="flex items-center space-x-4">
                        ${homeBtn}
                        ${adminBtn}
                        <span class="text-sm font-medium text-slate-400 hidden sm:block">Welcome, <span class="text-indigo-400">${currentUser.username}</span></span>
                        <button onclick="logout()" class="text-slate-400 hover:text-white text-sm font-bold bg-slate-800 px-4 py-2 rounded-lg border border-slate-700 transition-colors">Log Out</button>
                    </div>
                `;
    } else {
        actions.innerHTML = `
                    <div class="flex items-center space-x-2">
                        ${homeBtn}
                        <button onclick="router.login()" class="text-slate-400 hover:text-white text-sm font-bold px-4 py-2 transition-colors">Login</button>
                        <button onclick="router.signup()" class="bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-bold px-5 py-2 rounded-lg transition-all shadow-lg shadow-indigo-500/20">Sign Up</button>
                    </div>
                `;
    }
}

function logout() { currentUser = null; localStorage.removeItem('journal_user'); router.home(); showToast("Logged out"); }
function closeModal() { document.getElementById('modal-container').classList.add('hidden'); }
function showToast(m, t = 'success') {
    const toast = document.getElementById('toast'), msg = document.getElementById('toast-message'), icon = document.getElementById('toast-icon');
    msg.innerText = m; toast.classList.remove('hidden');
    if (t === 'error') { toast.children[0].classList.add('border-red-500'); icon.className = "fas fa-exclamation-triangle text-red-500"; }
    else { toast.children[0].classList.remove('border-red-500'); icon.className = "fas fa-check-circle text-green-500"; }
    setTimeout(() => toast.classList.remove('translate-y-20'), 10);
    setTimeout(() => { toast.classList.add('translate-y-20'); setTimeout(() => toast.classList.add('hidden'), 300); }, 3000);
}

function openCreateModal() {
    const modal = document.getElementById('modal-container'), content = document.getElementById('modal-content');
    modal.classList.remove('hidden');
    content.innerHTML = `
                <div class="flex justify-between items-center mb-6"><h3 class="text-2xl font-bold">New Entry</h3><button onclick="closeModal()" class="text-slate-400 hover:text-white"><i class="fas fa-times"></i></button></div>
                <form id="entry-form" class="space-y-4">
                    <div><label class="block text-sm font-medium mb-2">Title</label><input type="text" name="title" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"></div>
                    <div><label class="block text-sm font-medium mb-2">Content</label><textarea name="content" rows="6" class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 custom-scrollbar resize-none"></textarea></div>
                    <div><label class="block text-sm font-medium mb-2">Feeling</label><select name="sentiment" class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"><option value="HAPPY">Happy</option><option value="SAD">Sad</option><option value="ANGRY">Angry</option><option value="ANXIOUS">Anxious</option></select></div>
                    <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl transition-all mt-4 shadow-lg">Save</button>
                </form>`;
    document.getElementById('entry-form').onsubmit = async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = { title: fd.get('title'), content: fd.get('content'), sentiment: fd.get('sentiment') };
        if (await apiRequest('/journal/create', { method: 'POST', body: JSON.stringify(payload) })) { showToast("Saved!"); closeModal(); router.dashboard(); }
    };
}

async function openEditModal(id) {
    const e = state.entries.find(x => x.uuid === id);
    const modal = document.getElementById('modal-container'), content = document.getElementById('modal-content');
    modal.classList.remove('hidden');
    content.innerHTML = `
                <div class="flex justify-between items-center mb-6"><h3 class="text-2xl font-bold">Edit Entry</h3><button onclick="closeModal()" class="text-slate-400 hover:text-white"><i class="fas fa-times"></i></button></div>
                <form id="edit-form" class="space-y-4">
                    <div><label class="block text-sm font-medium mb-2">Title</label><input type="text" name="title" value="${e.title}" required class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"></div>
                    <div><label class="block text-sm font-medium mb-2">Content</label><textarea name="content" rows="6" class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500 resize-none">${e.content || ''}</textarea></div>
                    <div><label class="block text-sm font-medium mb-2">Feeling</label><select name="sentiment" class="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"><option value="HAPPY" ${e.sentiment === 'HAPPY' ? 'selected' : ''}>Happy</option><option value="SAD" ${e.sentiment === 'SAD' ? 'selected' : ''}>Sad</option><option value="ANGRY" ${e.sentiment === 'ANGRY' ? 'selected' : ''}>Angry</option><option value="ANXIOUS" ${e.sentiment === 'ANXIOUS' ? 'selected' : ''}>Anxious</option></select></div>
                    <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-4 rounded-xl transition-all mt-4">Update</button>
                </form>`;
    document.getElementById('edit-form').onsubmit = async (ev) => {
        ev.preventDefault();
        const fd = new FormData(ev.target);
        const payload = { title: fd.get('title'), content: fd.get('content'), sentiment: fd.get('sentiment') };
        if (await apiRequest(`/journal/update/${id}`, { method: 'PUT', body: JSON.stringify(payload) })) { showToast("Updated!"); closeModal(); router.dashboard(); }
    };
}

async function deleteEntry(id) { if (confirm("Delete entry?")) if (await apiRequest(`/journal/delete/${id}`, { method: 'DELETE' })) { showToast("Deleted"); router.dashboard(); } }

window.addEventListener('DOMContentLoaded', () => {
    const saved = localStorage.getItem('journal_user');
    if (saved) currentUser = JSON.parse(saved);
    router.home();
});

window.onclick = e => { if (e.target == document.getElementById('modal-container')) closeModal(); }
