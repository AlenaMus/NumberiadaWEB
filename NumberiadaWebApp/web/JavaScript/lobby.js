/**
 * Created by OR on 2/13/2017.
 */

window.myPlayersVersion = 0;
window.myGamesVersion = 0;
$(function () {
    $("#Error").hide();
    $("#logoutButton").click(onLogoutClick);
    $("#gameRoomButton").click(onGameRoomClick);
    $('input[type=file]').bootstrapFileInput();
    initilazeLoadGameForm();
});

$(window.intervalUpdates = setInterval(function ()
    {
        updatePlayers();
    }, 200)
);

// $(window.intervalUpdates = setInterval(function ()
//     {
//         updateGamesTable();
//     }, 200)
// );

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


function updateGamesTable() {
    $.ajax({
        type: 'POST',
        url: "updateGamesTable",
        data: {myGamesVersion: window.myGamesVersion},
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if (data.latestGameVersion !== window.myGamesVersion) {
                window.myGamesVersion = data.latestPlayersVersion;
                // $("#gamesTable > tr").remove();
                // $.each(data.games,
                //     addNewGame(game.gameTitle,game.playerName,game.boardSize,game.playersNumber,game.gameNumber));
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

function initilazeLoadGameForm() {

    $("#LoadFileForm").change(function (event) {
         $("#Error").hide();
        event.preventDefault();
        var formData = new FormData($(this)[0]);
        $.ajax({
            url: 'loadGameXML',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            timeout: 6000,
            success: function (gameData, textStatus, jqXHR) {
                if (!(gameData == null)) {
                    console.log("success load !");
                    addNewGame(gameData.gameTitle,gameData.userName,gameData.boardSize,gameData.numOfPlayers,gameData.gameNumber);
                }
                else
                    $("#Error").text("Loading Error.. " + gameData).show();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (textStatus === "timeout") {
                    $("#Error").text("Server Timeout. Try again..").show();
                }
                else {
                    $("#Error").text(errorThrown.toString()).show();
                }
                console.error(errorThrown.toString());
            }
        });
        return false;
    });



function addNewGame(gameTitle,playerName,boardSize,playersNumber,gameNumber){
        $("#gamesTable").append("<tr class=GameRow>" +
       "<td style=\"text-align: left;\">" + gameNumber + "</td>" +
        "<td style=\"text-align: left;\">" + gameTitle + "</td>" +
        "<td style=\"text-align: left;\">" + playerName + "</td>" +
        "<td style=\"text-align: left;\">" + boardSize + "</td>" +
         "<td style=\"text-align: left;\">" + playersNumber + "</td>" +
        "<td style=\"text-align:left;\">" + "<button type='submit'  id='showBoardId'>Show Game Board</button></td>"+
        "</tr>" );

    //set boardView


}


}
