const sendBtn = document.getElementById('send');
const mes = document.getElementById('message');
const receive = document.getElementById('receive');
const logout = document.getElementById('logout');

let stompClient = null;
const email = getCookie('email');
console.log("email ne:   +++++: "+email);

function getCookie(name) {
  let matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

const connect = ()  => {

    var socket = new SockJS('/websocket-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}
connect();


function onConnected() {
    // Subscribe to the Public Topic
  stompClient.subscribe('/user/queue/newMember',  (data) => console.log(data));

 stompClient.subscribe('/topic/newMember', data => console.log("data2: " + data.body));



    // Tell your username to the server
  sendMessage('/app/register', email);

    stompClient.subscribe(`/user/${email}/msg`,  data =>{
    console.log(`-------- received message:\n`+ data.body+`\n--------received message!!!!`);
    displayMessage(data);
  });


}

function onError(error) {
    console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
}

function sendMessage(url, message) {
    stompClient.send(url, {}, message);
}


const displayMessage = data =>{
let mess = JSON.parse(data.body);
console.log(" jhdsjfldsk:  "+mess);
receive.innerHTML = `from: `+mess.sender+`\n message: `+ mess.message+"\n to: "+mess.recipient;
}


const onDisconnect = () =>{
  sendMessage('/app/unregister', email);
  stompClient.disconnect();
}

sendBtn.addEventListener('click', () => {
  let messa = mes.value;
  sendMessage('/app/message', JSON.stringify({
    recipient: 'khang',
    sender: email,
    message: messa,
    type: 'PrivateMessage',
    messageType: 'Text'
  }));
  mes.innerHTML = '';
});

logout.addEventListener('click', onDisconnect);
