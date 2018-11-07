var room = "room1";
var user = "Me";
var baseUrl = "//localhost:8080/functional/";
var chat_;
var stream;

$(document).ready(function(){
    chat_ = $('.chat');

    initStreamHandler();

    $('#username').on('change', initStreamHandler);
    $('#room-select').on('change', initStreamHandler);

    $('#sendMessage').on('click', sendMessage);
    $('#message').keypress(function(e) {
        if(e.which == 13) {
            sendMessage(e);
            return false;
        }
    });
});

var initStreamHandler = function(){
    clearMessages();

    //Pobieramy dane
    room = $('#room-select').val();
    user = $('#username').val();
    console.log("Connecting user " + user + " to room " + room);
    if(typeof stream !== 'undefined'){
        stream.close();
    }
    //Tworzymy EventSource kierujące się do endpointa produkującego text/event-stream
    stream = new EventSource(baseUrl + "rooms/"+room);
    //Dodajemy funkcję obsługującą przychodzące eventy
    stream.addEventListener("message", function(event){
        //Jako, że dostajemy text, sparsujmy go do obiektu
        var messageObject = JSON.parse(event.data);
        //Obsłużmy UI
        displayMessage(messageObject);
    });
    stream.onerror = function(e){
        clearMessages();
        displayMessage({"content":"Retrying connection...", "user":"System"});
    };

    stream.onopen = function(e){
        clearMessages();
        displayMessage({"content":"Connected successfully", "user":"System"});
    };
};

var displayMessage = function(message){
    var cls = "message ";
    var photo = "66";
    if(message.user == user){
        cls += "me";
        photo = "67"
    }

    chat_.append('<div class="'+ cls +'">' +
        '<img src=\"http://api.randomuser.me/portraits/med/men/'+photo+'.jpg\" />' +
        '<div><p>'+message.content+'</p></div>' +
        '</div>');


};


var sendMessage = function(e){
    var messageContent=$('#message').val();
    var messageData = {
        content: messageContent,
        user: user,
        room: room
    };

    postMessage(messageData);

    console.log("Sending message: ", messageData);
    $('#message').val('').focus();
};

var postMessage = function(message){
    $.ajax({
        type: 'POST',
        url: baseUrl+"messages",
        data: JSON.stringify(message),
        dataType: 'json',
        contentType: 'application/json',
        success: function(){
            console.log("Successfully sent message.");
        }
    });
};

var clearMessages = function (){
    $('.message').remove();
};
