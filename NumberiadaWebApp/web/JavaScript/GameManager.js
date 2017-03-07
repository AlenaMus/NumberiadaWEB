window.playSound = true;
window.playerVerion = 0;

window.CurrPlayerIndex = 0;
//window.intervalUpdates = 0;
window.playersUpdates = 0;
window.intervalStartGame = 0;
window.intervalGameUpdates=0;
window.quited = false;

window.myPlayerIndex = 0;
window.isGameStarted = false;


$(function() {

    initilazeGame();
    checkGameStart();

 $(window.intervalGameUpdates = setInterval(function () {
            getGameUpdate();
        }, 200)
    );

 $(window.intervalStartGame = setInterval(function ()
        {
            checkGameStart();
        }, 300)
    );

 $(window.playersUpdates = setInterval(function ()
        {
            updateGamePlayersList();
        }, 300)
    );




$("#quitButton").click(function () {
        quit();
});
$("#SoundButton").click(function () {
        window.playSound === true ? mute() : sound();
});
    $("#BackGroundAudio").trigger("play");
});


function quit() {
    window.quited = true;
    setAction("quit");
}
function mute() {
window.playSound = false;
$("audio").trigger("pause");
$("#SoundButton").attr({
src: "resource/images/sound_off.png",
title: "Mute",
alt: "Mute"});
}


function sound() {
window.playSound = true;
$("#BackGroundAudio").trigger("play");
$("#SoundButton").attr({
src: "resource/images/sound on.jpg",
title: "Sound",
alt: "Sound"});
}



function checkGameStart() {
    $.ajax({
        type: 'POST',
        url: "Start-Game",
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if (data.succeedToStartGame === false) {
                $('#gameStatus').html(data.message);
            } else {
                window.isGameStarted = true;
                $('#gameStatus').html('Game Started!');
                clearInterval(window.intervalStartGame);
                if(data.message !== ""){
                    window.alert(data.message);
                }
                updateCurrentPlayer(data.currentPlayer);
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

function setAction(actionType)
{
    $.ajax({
    type: 'POST',
    data: {actionType: actionType, playerIndex: window.myPlayerIndex},
    url: "User-Iteration",
    timeout: 6000,
    success: function (data) {
        updateBoardAfterMove(data);
        handelComputerTurn(data);
    },
    error: function (textStatus) {
    if (textStatus === "timeout") {
    $("#Error").text("Server Timeout setAction. Try again..").show();
    }
    else {
    $("#Error").text("Something went wrong setAction. Try again..").show();
    }
    }
});
return false;
}

function getGameUpdate()
{
    $.ajax({
    type: 'POST',
    data: {myGameVerion: window.playerVerion}, //playerIndex: window.myPlayerIndex},
    url: "Game-Updates",
    dataType: "json",
    timeout: 6000,
    success: function (data, textStatus, jqXHR) {
    if (data !== false && data.latestGameVersion !== window.playerVerion)
    {
        updateBoardAfterMove(data);

     if (data.gameOver === true)
    {
        handelGameOver(data);
    }
    else {
            handelComputerTurn(data);
            updateCurrentPlayer(data.currentPlayer);
            //showPlayerTurn(data.currentPlayerIndex);
        }
     }
    },
    error: function (jqXHR, textStatus, errorThrown) {
    if (textStatus === "timeout") {
        $("#Error").text("Server Timeout getGameUpdate. Try again..").show();
    }
    else {
    $("#Error").text("Something went wrong getGameUpdate. Try again..").show();
    }
    }
    });
        return false;
}

function updateBoardAfterMove(data) {
    var time;
    time = data.computerTurn === true ? 500 : 200;

    if (data.cellToUpdate !== undefined && data.cellToUpdate !== null) {
        window.playerVerion++;
        window.CurrPlayerIndex = data.currentPlayerIndex;

        data.cellToUpdate.forEach(function (square) {

            var value = square.squareValue.value;
            var point = square.location;
            var color = square.color.value;
            var buttId = '#butt' + point.row + point.col;
            var classColor = chooseSquare(color);
            $(buttId).html(value);
            $(buttId).css("background-color", classColor);

        });
    }
}

function updateCurrentPlayer(currPlayer) {
    $("#currentPlayerName").text(currPlayer.name.value);
    $("#currentPlayerScore").text(currPlayer.score.value);
    $("#currentPlayerColor").text(currPlayer.color.value);
    $("#currentPlayerType").text(currPlayer.playerType.value.toString());

}

function handelComputerTurn(data) {

    if (data.computerTurn === true && data.allPlayersAreUpToDate === true) {
        setAction("computerIteration");
        }
    $("#endTurnButton").attr("disabled", data.computerTurn);
}


function handelGameOver(data) {
        $("#GameBoardContainer").replaceWith('<span id="win" class="label label-primary">' + data.winner + "</span>");
        clearInterval(window.intervalGameUpdates);
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
        case 3:setcolor = '#4CAF50';
            break;
        case 4:setcolor = '#f4d42b';
            break;
        case 5:setcolor = '#9d3b9c';
            break;
        case 6:setcolor = '#f8609f';
            break;
        case 100:setcolor ='#89F455';
            break;
    }
    return setcolor;
}



//$(function () {
// $("#BackButton").click(function () {
//  if (window.quited === false)
//  {
//   quit();
//  }
//  window.location.href = "mainMenu.html";
//
// });
// $("#Error").hide();
//
// initilazeGame();
//});

function updateGamePlayersList() {
    $.ajax({
        type: 'POST',
        url: "Get-Game-Players",
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            $("#playersInfoTable").empty();
              buildPlayersBar(data);
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




function initilazeGame() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: "Get-Game-Players",
        timeout: 6000,
        success: function (playersData, textStatus, jqXHR) {

            //window.myPlayerIndex = playersData.myPlayerIndex;
            //updateCurrentPlayer(playersData.currPlayer);
            buildPlayersBar(playersData);
            buildGameBoardStructure();
            //setCellClick();
            //showPlayerTurn(playersData.currPlayer);
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




function buildGameBoardStructure() {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: "getGameBoard",
        timeout: 6000,
        success: function (board, textStatus, jqXHR) {
            if(board!=='Error'){
                buildGameBoard(board.gameBoard,board.boardSize);
            }else{

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


function buildGameBoard(board,size) {

    var value;
    var color;
    var classColor;
    var boardRow;
    var buttId;
    var cell;

    var gameBoard = $('#GameBoardTable');

    for (var i = 0; i < size; i++) {
        boardRow = $('<tr>', {class: 'boardRow', align: 'center'});
        //boardRow.css()
        boardRow.appendTo(gameBoard);

        for (var j = 0; j < size; j++) {

            value = board[i][j].squareValue.value;
            color = board[i][j].color.value;
            classColor = chooseSquare(color);
            //classColor = chooseClass(color);
            buttId = 'butt'+ i+j;

            cell = $('<td>', {class: 'cell', align: 'center'});
            var div = $('<div>',{class:'divButt',align: 'center'});
            var button = $('<button/>', {class: 'square',text: value, id: 'butt', "row": i, "col": j,align: 'center',click:clickedSquare()});
            button.id = buttId;
            button.css("background-color",classColor);
            div.append(button);
            cell.append(div);
            cell.appendTo(boardRow);


        }
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
        case 3:setcolor = '#4CAF50';
            break;
        case 4:setcolor = '#f4d42b';
            break;
        case 5:setcolor = '#9d3b9c';
            break;
        case 6:setcolor = '#f8609f';
            break;
        case 100:setcolor ='#89F455';
            break;
    }
    return setcolor;
}



function clickedSquare(){
    if(window.isGameStarted == true) {

        var row = $(this).attr("row");
        var col = $(this).attr("col");

        $.ajax({
            type: 'POST',
            data: {row: row, col: col, actionType: "userIteration"},
            url: "User-Iteration",
            timeout: 6000,
            dataType: 'json',
            success: function (message) {
                console.log("pressed button success"+message);
                $('#gameStatus').html(message);
            },
            error: function (textStatus) {
                //setCellClick();
                $("#Error").text("A temporary problem").show();
            }
        });
    }

}

function buildPlayersBar(players) {
    for (var i = 0; i < players.numOfPlayers; i++) {
        var player = players.players[i];
        setPlayersBar(i, player);
    }
}

function setPlayersBar(index, player) {
    var playerRow = "<br><tr class=PlayerName>" +
        "<th>index</th>"+
        "<td style=\"text-align: left;\">" + player.name.value + "</td> "+
        "<td style=\"text-align: left;\">" + player.playerType.value + "</td>" +
        "<td style=\"text-align: left;\">" + player.score.value + "</td>" + "</tr>";

    $('#playersInfoTable').append(playerRow);

}






