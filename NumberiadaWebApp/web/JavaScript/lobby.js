/**
 * Created by OR on 2/13/2017.
 */

window.myPlayersVersion = 0;
window.myGamesVersion = 0;
window.mySignedPlayersVersion = 0;


$(function () {
    $("#Error").hide();
    $("#logoutButton").click(onLogoutClick);
  //  $("#gameButton").click(onGameRoomClick);
    $("#gameButton").click(getFuckingBoard);
    $("#signToGameButton").click(onSignToGameClick);
    $('input[type=file]').bootstrapFileInput();
     initilazeLoadGameForm();
});

$(window.intervalUpdates = setInterval(function ()
    {
        updatePlayers();
    }, 200)
);

$(window.intervalUpdates = setInterval(function ()
    {
        updateGamesTable();
    }, 200)
);


$(window.intervalUpdates = setInterval(function ()
    {
        updateSignedPlayers();

    }, 300)
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

function updateGamesTable() {
    $.ajax({
        type: 'POST',
        url: "updateGamesTable",
        data: {myGamesVersion: window.myGamesVersion},
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if(data.games !== null) {
                if (data.latestGameVersion !== window.myGamesVersion) {
                    window.myGamesVersion = data.latestGameVersion;
                    $("#gamesTable > tr").remove();
                    $.each(data.games, function (index, game) {
                        updateGamesTableView(game.gameNumber, game.gameTitle, game.userName, game.boardSize, game.numOfPlayers, game.signedPlayers, game.board);
                    });
                }
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


function onSignToGameClick(title,gameNumber)
{
    $.ajax({
        type: 'POST',
        url: "signUserToGame",
        data: {gameNumber: gameNumber,gameTitle:title},
        timeout: 6000,
        success: function (messageConfirm, textStatus, jqXHR) {
            if (messageConfirm.success == true)
            {
                $('#signToGame' + gameNumber).html(messageConfirm.playersUpdateNum);
                  window.mySignedPlayersVersion++;
                //window.location.href = "gameRoom.html";
            }
            else
            {
                $("#Error").text(messageConfirm.message).show();
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

    $("#Error").hide();
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
                if (gameData !== null) {
                    addNewGame(gameData.gameTitle, gameData.userName, gameData.boardSize, gameData.numOfPlayers, gameData.gameNumber, gameData.board.gameBoard);
                    myGamesVersion++;
                    updateGamesTable();
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
}

function updateSignedPlayers() {

    $.ajax({
        type: 'POST',
        url: "updateSignedPlayers",
        data: {mySignedPlayersVersion: window.mySignedPlayersVersion},
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if(window.mySignedPlayersVersion !== data.latestSignedPlayersVersion ) {
                window.mySignedPlayersVersion = data.latestSignedPlayersVersion;
                $.each(data.games, function (index, game) {
                    $('#signToGame' + game.gameNumber).html(game.signedPlayers);
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

// function updateGamesTableView(gameNumber,gameTitle,playerName,boardSize,playersNumber,signedPlayers,board) {
//         $("#gamesTable").append("<tr class=GameRow >" +
//         "<td style=\"text-align: left;\">" + gameNumber + "</td>" +
//         "<td style=\"text-align: left;\">" + gameTitle + "</td>" +
//         "<td style=\"text-align: left;\">" + playerName + "</td>" +
//         "<td style=\"text-align: left;\">" + boardSize + 'X' + boardSize + "</td>" +
//         "<td style=\"text-align: left;\">" + playersNumber + "</td>" +
//         "<td style=\"text-align: left; \" valign = bottom><div id = 'playersNumber' style = 'width:100%'></div><button class ='SignToGameButton'>Sign into game</button></td>" +
//         "<td style=\"text-align:left;\">" + "<button type='submit'  id='showBoardId' >Show Game Board</button>" +
//         "</td>" +
//         "</tr>");
//
//    // var signedInPlayersID = document.getElementById('playersNumber');
//      var id = 'signToGame'+ gameNumber;
//     $('#playersNumber').attr('id',id);
//     $('#'+id).html(signedPlayers);
//     //signedInPlayersID.id = 'signToGame'+ gameNumber;
//    // $('#'+signedInPlayersID.id).html(signedPlayers);
//
//     var gameBoardView = document.getElementById('showBoardId');
//     gameBoardView.id = 'showBoardId'+ gameNumber;
//     $('#'+ gameBoardView.id).click(function() {
//         var title = $(this).closest("tr").find("td").eq(1).html();
//         var gameNumber = $(this).closest("tr").find("td").eq(0).html();
//         gameBoardPreview(title,gameNumber);
//
//     });
//
//     $('.SignToGameButton').click(function () {
//         var title = $(this).closest("tr").find("td").eq(1).html();
//         var gameNumber = $(this).closest("tr").find("td").eq(0).html();
//         onSignToGameClick(title,gameNumber);
//     });
//
// }


function updateGamesTableView(gameNumber,gameTitle,playerName,boardSize,playersNumber,signedPlayers,board) {
    $("#gamesTable").append("<tr class=GameRow>" +
        "<td style=\"text-align: left;\">" + gameNumber + "</td>" +
        "<td style=\"text-align: left;\">" + gameTitle + "</td>" +
        "<td style=\"text-align: left;\">" + playerName + "</td>" +
        "<td style=\"text-align: left;\">" + boardSize + 'X' + boardSize + "</td>" +
        "<td style=\"text-align: left;\">" + playersNumber + "</td>" +
        "<td style=\"text-align: left; \" valign = bottom><div id = 'playerNumber' style = 'width:100%'></div><button class ='SignToGameButton'>Sign into game</button></td>" +
        "<td style=\"text-align:left;\">" + "<button type='submit'  id='showBoardId' >Show Game Board</button>" +
        "</td>" +
        "</tr>");


    var signedInPlayersID = document.getElementById('playerNumber');
    signedInPlayersID.id = 'signToGame'+ gameNumber;
    $('#'+signedInPlayersID.id).html(signedPlayers);


    var gameBoardView = document.getElementById('showBoardId');
    gameBoardView.id = 'showBoardId' + gameNumber;

    $('#'+ gameBoardView.id).click(function() {
        var title = $(this).closest("tr").find("td").eq(1).html();
        var gameNumber = $(this).closest("tr").find("td").eq(0).html();
        gameBoardPreview(title,gameNumber);

    });

    $('.SignToGameButton').click(function () {
        var title = $(this).closest("tr").find("td").eq(1).html();
        var gameNumber = $(this).closest("tr").find("td").eq(0).html();
        onSignToGameClick(title,gameNumber);
    });

}








function getFuckingBoard(){
    window.location.href = 'GameRoom.html';
}


function gameBoardPreview(gameTitle,gameNumber) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        data: {gameTitle: gameTitle},
        url: "updateBoard",
        timeout: 6000,
        success: function (board, textStatus, jqXHR) {
            var newDiv = $(document.createElement('div'));
            newDiv.html(createBoard(board.gameBoard, board.boardSize, gameNumber));
            newDiv.dialog({
                modal: true,
                title: "Game Board",
                height: "auto",
                width: "auto",
                close: function (event, ui) {
                    $('#board').empty();
                }
            });
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

    function addNewGame(gameTitle, playerName, boardSize, playersNumber, gameNumber,board) {
        updateGamesTableView(gameNumber,gameTitle,playerName,boardSize,playersNumber,0,board);

    }


function createBoard(board, size, gameNumber) {
    var gameBoard = board;
    var value;
    var color;
    var classColor;

    var result = $('#board');
    for (var i = 0; i < size; i++) {
        result.append("<tr class ='boardRow'>");
        for (var j = 0; j < size; j++) {
            value = gameBoard[i][j].squareValue.value;
            color = gameBoard[i][j].color.value;
            classColor = chooseSquare(color);
            var idd= i.toString() + j.toString();
            result.append("<td class='cell'><div class='divButt'><button class='square' id='boardButton' onclick='chooseSquare()'>"+value+"</button></div></td>");
            var button = document.getElementById('boardButton');
            button.class = 'square'+color;
            button.id = idd;
            $('#'+button.id).css("background-color",classColor);
        }
        result.append("</tr>");
    }

    return result;
}


    function chooseSquare(color){
        var setcolor;
        switch (color){
            case 0: setcolor = '#b8a3a3';
                break;
            case 1:setcolor = '#f44336';
                break;
            case 2:setcolor = '#008CBA';
                break;
            case 3:setcolor = '#00bb5e';
                break;
            case 4:setcolor = '#ffda0a';
                break;
            case 5:setcolor = '#9d3b9c';
                break;
            case 6:setcolor = '#f8609f';
                break;
            case 100:setcolor = '#89F455';
                break;
        }
        return setcolor;
    }







