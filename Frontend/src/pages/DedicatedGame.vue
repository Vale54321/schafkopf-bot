<script lang="ts" setup>
import {onMounted, ref} from 'vue';

import {scg} from 'ioc-service-container';
import CardComp from '../components/CardComponent.vue';
import {BackendMessage, Card, GamePhase, GameState, MessageType} from "../BackendMessage";

const backendConnection = scg("BackendConnection");


const messageFromServer = ref<string[]>([]);

const gameStateText = ref<string>("Schafkopf");
const gameInfoText = ref<string>("");

const socket = ref<WebSocket | null>();
const tableCards = ref<Card[]>([]);
const botCards = ref<Card[]>();
const trickCard = ref<Card>();

const showGameSelect = ref(true);

function startDedicated(): void {
  backendConnection.sendMessage(MessageType.START_DEDICATED_GAME,);
}

function joinGame(): void {
  backendConnection.sendMessage(MessageType.JOIN_GAME,);
}

function sendCard(cardInput: Card): void {
  const index = botCards.value!.findIndex(card => card === cardInput);

  // If card exists in the array, remove it
  if (index !== -1) {
    botCards.value!.splice(index, 1);
  }
  backendConnection.sendMessage(MessageType.PLAYER_CARD, {card: cardInput});
}

function showGameState(gamestate: GameState) {


  switch (gamestate.gamePhase) {
    case GamePhase.GAME_START:
      gameStateText.value = "Spiel startet";
      showGameSelect.value = false;
      // botCards.value = 0;
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
        // botCards.value.pop();
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
  socket.value = backendConnection.getWebSocket();

  const messageListener = (message: string) => {
    const message1: BackendMessage = JSON.parse(message);
    console.log(message1)
    if (message1.message_type === "GAME_STATE" && "gamestate" in message1.content) {
      console.log(message1.content)
      showGameState(message1.content.gamestate)
    }
    if (message1.message_type === "ONLINE_PLAYER_HAND" && "cards" in message1.content) {
      botCards.value = message1.content.cards;
      console.log(message1.content.cards)
    }
  };

  backendConnection.addMessageListener(messageListener);
});
</script>
<template>
  <div>
    <div v-for="message in messageFromServer" :key="message">{{ message }}</div>

    <div v-if="showGameSelect">
      <div class="flex gap-2 place-content-center">
        <button class="v-button" @click="startDedicated">Starten</button>
        <button class="v-button" @click="joinGame">Join</button>
      </div>
    </div>
    <div v-else>
      <div class="flex gap-2 place-content-center">
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
          <CardComp v-for="card in botCards" :card="card" class="sm" @click="sendCard(card)"/>
        </div>
      </div>
    </div>
  </div>

  <router-view/>
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
