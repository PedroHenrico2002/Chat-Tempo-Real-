let stompClient = null;
let username = null;
let currentRoom = 'geral';
let subscriptions = [];

const loginPage = document.getElementById('login-page');
const chatPage = document.getElementById('chat-page');
const usernameInput = document.getElementById('username-input');
const loginBtn = document.getElementById('login-btn');
const roomList = document.getElementById('room-list');
const messagesDiv = document.getElementById('messages');
const messageInput = document.getElementById('message-input');
const sendBtn = document.getElementById('send-btn');
const currentRoomLabel = document.getElementById('current-room');
const usersList = document.getElementById('users-list');
const onlineCount = document.getElementById('online-count');
const newRoomInput = document.getElementById('new-room-input');
const createRoomBtn = document.getElementById('create-room-btn');

loginBtn.addEventListener('click', login);
usernameInput.addEventListener('keypress', (e) => { if (e.key === 'Enter') login(); });
sendBtn.addEventListener('click', sendMessage);
messageInput.addEventListener('keypress', (e) => { if (e.key === 'Enter') sendMessage(); });
createRoomBtn.addEventListener('click', createRoom);
newRoomInput.addEventListener('keypress', (e) => { if (e.key === 'Enter') createRoom(); });

function login() {
    username = usernameInput.value.trim();
    if (!username) return;
    loginPage.classList.add('hidden');
    chatPage.classList.remove('hidden');
    connect();
}

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    loadRooms();
    joinRoom(currentRoom);
}

function onError(error) {
    console.error('Erro na conexão:', error);
    alert('Não foi possível conectar ao chat.');
}

function joinRoom(roomId) {
    currentRoom = roomId;
    currentRoomLabel.textContent = roomId.charAt(0).toUpperCase() + roomId.slice(1);
    messagesDiv.innerHTML = '';

    subscriptions.forEach(sub => sub.unsubscribe());
    subscriptions = [];

    const msgSub = stompClient.subscribe('/topic/' + roomId, onMessageReceived);
    const usersSub = stompClient.subscribe('/topic/' + roomId + '/users', onUsersReceived);
    subscriptions.push(msgSub, usersSub);

    stompClient.send('/app/chat/' + roomId + '/join', {}, JSON.stringify({
        sender: username,
        room: roomId,
        type: 'JOIN'
    }));

    updateRoomListActive();
}

function leaveRoom(roomId) {
    stompClient.send('/app/chat/' + roomId + '/leave', {}, JSON.stringify({
        sender: username,
        room: roomId,
        type: 'LEAVE'
    }));
}

function sendMessage() {
    const content = messageInput.value.trim();
    if (!content || !stompClient) return;

    const message = {
        sender: username,
        content: content,
        room: currentRoom,
        type: 'CHAT'
    };

    stompClient.send('/app/chat/' + currentRoom + '/send', {}, JSON.stringify(message));
    messageInput.value = '';
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    displayMessage(message);
}

function onUsersReceived(payload) {
    const data = JSON.parse(payload.body);
    updateUsersList(data.users || []);
}

function displayMessage(message) {
    const msgDiv = document.createElement('div');
    msgDiv.classList.add('message');

    if (message.type === 'CHAT') {
        msgDiv.classList.add(message.sender === username ? 'own' : 'other');
        msgDiv.innerHTML = `<div class="sender">${escapeHtml(message.sender)}</div>${escapeHtml(message.content)}`;
    } else {
        msgDiv.classList.add('system');
        msgDiv.textContent = message.content;
    }

    messagesDiv.appendChild(msgDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function updateUsersList(users) {
    usersList.innerHTML = '';
    users.forEach(user => {
        const li = document.createElement('li');
        li.textContent = escapeHtml(user);
        usersList.appendChild(li);
    });
    onlineCount.textContent = users.length + ' online';
}

function loadRooms() {
    fetch('/api/rooms')
        .then(res => res.json())
        .then(rooms => {
            roomList.innerHTML = '';
            rooms.forEach(room => {
                const li = document.createElement('li');
                li.textContent = room.name;
                li.dataset.id = room.id;
                li.addEventListener('click', () => {
                    if (currentRoom !== room.id) {
                        leaveRoom(currentRoom);
                        joinRoom(room.id);
                    }
                });
                roomList.appendChild(li);
            });
            updateRoomListActive();
        });
}

function createRoom() {
    const name = newRoomInput.value.trim();
    if (!name) return;

    fetch('/api/rooms?name=' + encodeURIComponent(name), { method: 'POST' })
        .then(res => res.json())
        .then(room => {
            newRoomInput.value = '';
            loadRooms();
            leaveRoom(currentRoom);
            joinRoom(room.id);
        });
}

function updateRoomListActive() {
    Array.from(roomList.children).forEach(li => {
        li.classList.toggle('active', li.dataset.id === currentRoom);
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

