<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import CardComp from './components/CardComponent.vue';
import {BackendMessage, Card, GamePhase, GameState} from "./BackendMessage";

const messageFromServer = ref<string[]>([]);

const gameStateText = ref<string>("Schafkopf");
const gameInfoText = ref<string>("");

const socket = ref<WebSocket | null>();
const tableCards = ref<Card[]>([]);
const botCards = ref(0);
const trickCard = ref<Card>();

const showGameSelect = ref(true);


function startSimulation(): void {
  sendMessageToServer("startsimulation");

}

function stopSimulation(): void {
  sendMessageToServer("stopsimulation");
}

function showTrumpf(): void {
  tableCards.value = [];
  sendMessageToServer("showtrumpf");
}

function showFarben(): void {
  tableCards.value = [];
  sendMessageToServer("showfarben");
}

function setGame(game: string): void {
  sendMessageToServer(game);
}


function sendMessageToServer(message: string) {
  if (socket.value) {
    socket.value.send(message);
    console.log("Sent message to server:", message);
  }
}

function showGameState(gamestate: GameState) {


  switch (gamestate.gamePhase) {
    case GamePhase.GAME_START:
      gameStateText.value = "Spiel startet";
      showGameSelect.value = false;
      botCards.value = 8;
      break;
    case GamePhase.TRICK_START:
      gameStateText.value = "Runde startet";
      tableCards.value = [];
      trickCard.value = undefined
      gameInfoText.value = "";
      break;
    case GamePhase.WAIT_FOR_CARD:
      gameStateText.value = "Spieler " + gamestate.currentPlayer + " muss eine Karte legen.";
      break;
    case GamePhase.PLAYER_CARD:
      gameStateText.value = "Spieler " + gamestate.currentPlayer + " hat eine Karte gespielt.";
      if (gamestate.currentPlayer === 0) {
        botCards.value--
      }
      if (gamestate.trumpf) {
        gameInfoText.value = "TRUMPF";
      } else {
        gameInfoText.value = gamestate.color?.toString() ?? "ERROR";
      }
      tableCards.value.push(gamestate.card!);


      break;
    case GamePhase.PLAYER_TRICK:
      gameStateText.value = "Spieler " + gamestate.currentPlayer + " sticht.";
      trickCard.value = gamestate.card
      break;
    case GamePhase.GAME_STOP:
      showGameSelect.value = true;
      break;
    default:
      gameStateText.value = "Fehler";

  }

}

onMounted(() => {
  const websocketIp = import.meta.env.VITE_APP_WEBSOCKET_IP;
  // Open a WebSocket connection when the component is mounted
  socket.value = new WebSocket("ws://" + websocketIp + ":8080/schafkopf-events/");

  // Handle connection opened
  socket.value.addEventListener("open", (event) => {
    console.log("WebSocket connection opened:", event);
  });

  // Handle messages received from the server
  socket.value.addEventListener("message", (event) => {
    const message: BackendMessage = JSON.parse(event.data);
    console.log(message)
    if ('gamestate' in message) {
      console.log(message.gamestate)
      showGameState(message.gamestate)
    } else {
      console.log("Invalid BackendMessage format: ", event);
    }
  });

  // Handle connection closed
  socket.value.addEventListener("close", (event) => {
    console.log("WebSocket connection closed:", event);
  });

  // Handle errors
  socket.value.addEventListener("error", (event) => {
    console.error("WebSocket error:", event);
  });
});
</script>
<template>
  <div>
    <div v-for="message in messageFromServer" :key="message">{{ message }}</div>

    <div v-if="showGameSelect">
      <div class="flex gap-2 place-content-center">
        <button @click="setGame('setgame:sauspiel')">Sauspiel</button>

        <button @click="setGame('setgame:herzsolo')">herzsolo</button>
        <button @click="setGame('setgame:eichelsolo')">eichelsolo</button>
        <button @click="setGame('setgame:blattsolo')">blattsolo</button>
        <button @click="setGame('setgame:schellsolo')">schellsolo</button>

        <button @click="setGame('setgame:eichelwenz')">eichelwenz</button>
        <button @click="setGame('setgame:blattwenz')">blattwenz</button>
        <button @click="setGame('setgame:herzwenz')">herzwenz</button>
        <button @click="setGame('setgame:schellwenz')">schellwenz</button>

        <button @click="setGame('setgame:eichelgeier')">eichelgeier</button>
        <button @click="setGame('setgame:blattgeier')">blattgeier</button>
        <button @click="setGame('setgame:herzgeier')">herzgeier</button>
        <button @click="setGame('setgame:schellgeier')">schellgeier</button>

        <button @click="setGame('setgame:geier')">Geier</button>
        <button @click="setGame('setgame:wenz')">Wenz</button>
      </div>
      <div class="flex gap-2 place-content-center">
        <button @click="showFarben">Zeige alle Farben</button>
        <button @click="showTrumpf">Zeige alle Trumpfkarten</button>
      </div>
      <div class="flex gap-2 place-content-center">
        <button class="v-button" @click="startSimulation">Starten</button>
        <button class="v-button" @click="stopSimulation">Stoppen</button>
      </div>
    </div>
    <div v-else>
      <div class="flex gap-2 place-content-center">
        <button class="v-button" @click="stopSimulation">Stoppen</button>
      </div>
      <h1 class=" top-52 text-white font-bold text-6xl absolute text-center w-full">{{ gameInfoText }}</h1>
      <h1 class=" top-64 text-white font-bold text-6xl absolute text-center w-full">{{ gameStateText }}</h1>
      <div v-if="tableCards.length > 0">
        <!--      <div class="grid grid-cols-4 place-content-center">-->
        <!--        <CardComp v-for="card in tableCards" :card="card" class="md" />-->
        <!--      </div>-->
        <CardComp v-if="tableCards.length > 0" :card="tableCards[0]" class="absolute  card1  md"/>
        <CardComp v-if="tableCards.length > 1" :card="tableCards[1]" class="absolute  card2  md"/>
        <CardComp v-if="tableCards.length > 2" :card="tableCards[2]" class="absolute  card3  md"/>
        <CardComp v-if="tableCards.length > 3" :card="tableCards[3]" class="absolute  card4  md"/>
      </div>
      <div class="absolute left-0 top-1/2 transform  -translate-y-1/2">
        <CardComp v-if="trickCard" :card="trickCard" class="xl"/>
      </div>

      <div class="absolute bottom-0 w-full">
        <div class="flex flex-row gap-3 w-fit mx-auto justify-center">
          <CardComp v-for="i in botCards" :key="i" :card="Card.BACK" class="sm"/>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss">
$card-height: 24rem;

.card0 {
  z-index: 1;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.card1 {
  z-index: 1;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.card2 {
  z-index: 2;
  top: calc(50% + ($card-height * 0.10));
  left: calc(50% - ($card-height * 0.3));
  transform: rotate(50deg) translate(-50%, -50%);
}

.card3 {
  z-index: 3;
  top: calc(50% - ($card-height * 0.125));
  left: calc(50% + ($card-height * 0.35));
  transform: rotate(-30deg) translate(-50%, -50%);
}

.card4 {
  z-index: 4;
  top: calc(50% - ($card-height * 0.4));
  left: calc(50% + ($card-height * 0.35));
  transform: rotate(-60deg) translate(-50%, -50%);
}
</style>
