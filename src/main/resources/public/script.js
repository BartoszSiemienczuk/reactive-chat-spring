var room = "room1";
var user = "Me";
var baseUrl = "//localhost:8080/";
var chat_;

$(document).ready(function(){
    chat_ = $('.chat');

    var stream = new EventSource(baseUrl + "rooms/"+room);
    stream.addEventListener("message", function(event){
        var messageObject = JSON.parse(event.data);
        displayMessage(messageObject);
    });

    $('#sendMessage').on('click', function(e){
        e.preventDefault();
        var messageContent=$('#message').val();
        var messageData = {
            content: messageContent,
            user: user,
            room: room
        };

        postMessage(messageData);

        console.log("Sending message: ", messageData);
        $('#message').val('');
    });

    var displayMessage = function(message){
        var cls = "message ";
        if(message.user == user){
            cls += "me";
        }

        chat_.append('<div class="'+ cls +'">' +
            '<img src=\"http://api.randomuser.me/portraits/med/men/66.jpg\" />' +
            '<div><p>'+message.content+'</p></div>' +
            '</div>');


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
    }
});