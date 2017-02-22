/**
 * Created by OR on 2/13/2017.
 */

window.myPlayersVersion = 0;
$(function () {
    $("#Error").hide();
    $("#logoutButton").click(onLogoutClick);
    $("#gameRoomButton").click(onGameRoomClick);
});

$(window.intervalUpdates = setInterval(function ()
    {
        updatePlayers();
    }, 200)
);

function updatePlayers() {
    $.ajax({
        type: 'POST',
        url: "updatePlayers",
        data: {myPlayersVersion: window.myPlayersVersion},
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if (data.latestPlayersVersion !== window.myPlayersVersion) {
                window.myPlayersVersion = data.latestPlayersVersion;
                $("#usersList > tr").remove();
                $.each(data.players, function (index, player) {
                    $("#usersList").append("<tr class=PlayerRow>" +
                        "<td style=\"text-align: left;\">" + player + "</td>" +
                        "</tr>");
                });
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "timeout") {
                $("#Error").text("Server Timeout setAction. Try again..").show();
            }
            else {
                $("#Error").text("Something went wrong setAction. Try again..").show();
            }
        }
    });
}

function onLogoutClick()
{
    $.ajax({
        type: 'POST',
        url: "logout",
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            window.location.href = "index.html";
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "timeout") {
                $("#Error").text("Server Timeout setAction. Try again..").show();
            }
            else {
                $("#Error").text("Something went wrong setAction. Try again..").show();
            }
        }
    });
}

function onGameRoomClick()
{
    window.location.href = "GameRoom.html";
}

var fileForm = document.getElementById("LoadFileForm");
var fileSelect = document.getElementById("LoadFileInput");
var fileSpan = document.getElementById("uploadGame");


fileForm.onSubmit = function(event) {
    event.preventDefault();
    fileSpan.InnerHTML = "Uploading File";
    var file = fileSelect.files[0];
    if (!file.type.match("application/xml")) {
        $("#Error").text("Invalid file type , please upload only xml files").show();
    }
    var formData = new FormData();
    formData.append('gameFile',file);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'LoadGameXML', true);
    xhr.onLoad = function () {
        if (xhr.status === 200) {
            // File(s) uploaded.
           fileSpan.innerHTML = 'Uploaded';
        } else {
            alert('An error occurred while uploading!');
        }
    };
    xhr.send(formData);
}

// $.ajax({
//     type: 'POST',
//     url: "LoadGameXML",
//     data: formData,
//     dataType: 'xml',
//     timeout: 6000,
//     success: function (data, textStatus, jqXHR) {
//         fileSpan.innerHTML = 'Uploaded';
//     },
//     error: function (jqXHR, textStatus, errorThrown) {
//         if (textStatus === "timeout") {
//             $("#Error").text("Server Timeout setAction. Try again..").show();
//         }
//         else {
//             $("#Error").text("An error occurred while uploading!").show();
//         }
//     }
// });