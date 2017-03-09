window.playSound = true;
window.playerVerion = 0;
window.CurrPlayerIndex = 0;
window.intervalUpdates;
window.quited = false;

$(function ()
{
    $("#endTurnButton").click(function () {
        setAction("endTurn");
    });
    $("#quitButton").click(function () {
        quit();
    });
    $("#SoundButton").click(function () {
        window.playSound === true ? mute() : sound();
    });
    $("#BackGroundAudio").trigger("play");
});

$(window.intervalUpdates = setInterval(function ()
{
    getGameUpdate();
}, 200)
        );

$( setInterval(function ()
    {
        updateTimer();
    }, 200)
);

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

function setAction(actionType)
{
    $.ajax({
        type: 'POST',
        data: {actionType: actionType, playerIndex: window.myPlayerIndex},
        url: "User-Iteration",
        timeout: 6000,
        success: function (data) {
            updateGame(data);
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
        data: {myGameVerion: window.playerVerion, playerIndex: window.myPlayerIndex},
        url: "Game-Updates",
                dataType: "json",
                timeout: 6000,
                success: function (data, textStatus, jqXHR) {
                updateTimer(data);
            if (data.isTimeOutForThisPlayer === true) {
                quit();
                getGameUpdate();
            }
            else if (data !== false && data.latestGameVersion !== window.playerVerion)
            {
                updateGame(data);
                if (data.gameOver === true)
                {
                    handelGameOver(data);
                } else
                {
                    handelComputerTurn(data);
                    showPlayerTurn(data.currentPlayerIndex);
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

function updateGame(data)
{
    var time;
    time = data.computerTurn === true ? 500 : 200;

    if (data.cellToUpdate !== undefined && data.cellToUpdate !== null)
    {
        window.playerVerion++;
        window.CurrPlayerIndex = data.currentPlayerIndex;
        data.cellToUpdate.forEach(function (Minicell) {
            var point = Minicell.point;
            var color = Minicell.color;
            $('button[row = "' + point.x + '"][ col = "' + point.y + '"]').animate({backgroundColor: color}, time);
            $('button[row = "' + point.x + '"][ col = "' + point.y + '"]').attr("disabled", !Minicell.state);
            if (window.myPlayerIndex !== data.currentPlayerIndex) {
                $('button[row = "' + point.x + '"][ col = "' + point.y + '"]').attr("disabled", true);
            }
        });
        deletePlayerAvatarByIndex(data.DeletedPlayerIndex, data.gameOver);
    }
}

function updateTimer()
{
    $.ajax({
        type: 'POST',
        url: "Get-Player-Time",
        dataType: "json",
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if (window.myPlayerIndex === data.currentPlayerIndex)
            {
                document.getElementById("TimerTitel").innerHTML =  "Time left to you'r turn:";
            }
            else
            {
                document.getElementById("TimerTitel").innerHTML =  "Time Left to opponent turn:";
            }
            document.getElementById("Timer").innerHTML = data.timeLeft;
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

function handelComputerTurn(data) {

    if (data.computerTurn === true && data.allPlayersAreUpToDate === true) {
        setAction("computerIteration");
    }
    $("#endTurnButton").attr("disabled", data.computerTurn);
}

function handelGameOver(data) {
    $("#GameBoardContainer").replaceWith('<span id="win" class="label label-primary">' + data.winner + "</span>");
    clearInterval(window.intervalUpdates);
}

function deletePlayerAvatarByIndex(playerIndex, gameOver) {
    if (playerIndex !== -1)
    {
        $('div[index="' + playerIndex + '"]').remove();

        if (window.myPlayerIndex === playerIndex && gameOver !== true)
        {
            clearInterval(window.intervalUpdates);
            window.location.href = "mainMenu.html";
        }
    }
}
/*
function setTimer()
{
    var minutes = 0;
    var seconds = 20;

// Update the count down every 1 second
    window.timer = setInterval(function() {

        // Output the result in an element with id="demo"
        document.getElementById("Timer").innerHTML =  minutes + "m " + seconds + "s ";
        if (seconds - 1 < 0)
        {
            if(minutes - 1 < 0)
            {
                clearInterval(window.timer);
                document.getElementById("Timer").innerHTML = "Time's UP!";
            }
            else
            {
                minutes--;
                seconds = 59;
            }
        }
        else
        {
            seconds--;
        }
    }, 1000);
}*/



