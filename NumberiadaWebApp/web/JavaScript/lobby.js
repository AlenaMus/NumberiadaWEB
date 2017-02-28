/**
 * Created by OR on 2/13/2017.
 */

window.myPlayersVersion = 0;
window.myGamesVersion=0;


$(function () {
    $("#Error").hide();
    $("#logoutButton").click(onLogoutClick);
    $("#gameRoomButton").click(onGameRoomClick);
    $('input[type=file]').bootstrapFileInput();
     initilazeLoadGameForm();
    $('#showBoardId').click(function () {
        window.location.href = 'boardGame.html';
    });
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
                window.myGamesVersion = data.latestGameVersion;
                $("#gamesTable > tr").remove();
                $.each(data.games, function (index, game) {
                    $("#gamesTable").append("<tr class=GameRow>" +
                        "<td style=\"text-align: left;\">" + game.gameNumber + "</td>" +
                        "<td style=\"text-align: left;\">" + game.gameTitle + "</td>" +
                        "<td style=\"text-align: left;\">" + game.userName + "</td>" +
                        "<td style=\"text-align: left;\">" + game.boardSize + "</td>" +
                        "<td style=\"text-align: left;\">" + game.numOfPlayers + "</td>" +
                        "<td style=\"text-align: left;\">" + 'signed players' + "</td>" +
                        "<td style=\"text-align:left;\">" + "<button type='submit'  id='showBoardId' >Show Game Board</button></td>"+
                        "</tr>" );
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
                    console.log("success load !");
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


    function addNewGame(gameTitle, playerName, boardSize, playersNumber, gameNumber, board) {
        $("#gamesTable").append("<tr class=GameRow>" +
            "<td style=\"text-align: left;\">" + gameNumber + "</td>" +
            "<td style=\"text-align: left;\">" + gameTitle + "</td>" +
            "<td style=\"text-align: left;\">" + playerName + "</td>" +
            "<td style=\"text-align: left;\">" + boardSize + 'X' + boardSize + "</td>" +
            "<td style=\"text-align: left;\">" + playersNumber + "</td>" +
            "<td style=\"text-align: left;\">" + "Signed Players" + "</td>" +
            "<td style=\"text-align:left;\">" + "<button type='submit'  id='showBoardId' >Show Game Board</button>" +
            //  "<div id='myModal' class='modal'>" + "<div class='modal-content' id='boardId'><table class ='boardTable' id='board'></table><span class='close'>&times;</span></div></div>"+
            "</td>" +
            "</tr>");

        $('#showBoardId').click(function () {
            createBoard(board,boardSize,gameTitle);
        });

    }


    function createBoard(board, size, gameTitle) {

        var gameBoard = board;
        var value;
        var color;
        var classColor;
        var result = $('#board');
        for (var i = 0; i < size; i++) {
            result.append("<tr class ='boardRow'>"); //result+="<tr class ='boardRow'>"
            for (var j = 0; j < size; j++) {
                value = gameBoard[i][j].squareValue.value;
                color = gameBoard[i][j].color.value;
                classColor = chooseSquare(color);
                var idd= i.toString();
                idd+=j.toString();
                result.append("<td class='tdBoard'><button class='square' id='boardButton' onclick='chooseSquare()'>"+value+"</button></td>");
                var button = document.getElementById('boardButton');
                 button.id = idd;
                $('#'+button.id).css("background-color",classColor);
            }
            result.append("</tr>");
        }
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
        }
        return setcolor;
    }

    function buildBoard(title) {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            data: {gameTitle: title},
            url: "updateBoard",
            timeout: 6000,
            success: function (board, textStatus, jqXHR) {
                console.log(board);
                console.log(board.gameBoard);
                createBoard(board.gameBoard, board.boardSize, title);
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





