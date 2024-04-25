<script lang="ts" setup>
import {onMounted, ref} from 'vue';

import {scg} from 'ioc-service-container';
import CardComp from '../components/CardComponent.vue';
import {BackendMessage, Card, GamePhase, GameSession, GameState, MessageType} from "../BackendMessage";
import {useRouter} from "vue-router";

const backendConnection = scg("BackendConnection");


const messageFromServer = ref<string[]>([]);

const gameStateText = ref<string>("Spiel startet...");
const gameInfoText = ref<string>("");
const router = useRouter();

const socket = ref<WebSocket | null>();
const tableCards = ref<Card[]>([]);
const botCards = ref<Card[]>();
const trickCard = ref<Card>();
const gameState = ref<GameState>();


const gameSession = ref<GameSession>({
  serverName: "",
  playerCount: 0,
  players: []
});

function sendCard(cardInput: Card): void {
  backendConnection.sendMessage(MessageType.PLAYER_CARD, {card: cardInput});
}

async function showGameState(gamestate: GameState) {


  switch (gamestate.gamePhase) {
    case GamePhase.GAME_START:
      gameStateText.value = "Spiel startet";
      break;
    case GamePhase.TRICK_START:
      gameStateText.value = "Runde startet";
      tableCards.value = [];
      trickCard.value = undefined
      gameInfoText.value = "";
      break;
    case GamePhase.WAIT_FOR_CARD:
      gameState.value = gamestate;
      gameStateText.value = gamestate.currentPlayer + " muss eine Karte legen.";
      break;
    case GamePhase.PLAYER_CARD:
      gameStateText.value = gamestate.currentPlayer + " hat eine Karte gespielt.";

      if (gamestate.trumpf) {
        gameInfoText.value = "TRUMPF";
      } else {
        gameInfoText.value = gamestate.color?.toString() ?? "ERROR";
      }
      tableCards.value.push(gamestate.card!);


      break;
    case GamePhase.PLAYER_TRICK:
      gameStateText.value = gamestate.currentPlayer + " sticht.";
      trickCard.value = gamestate.card
      break;
    case GamePhase.GAME_STOP:
      await router.push("/gamesession");
      break;
    default:
      gameStateText.value = "Fehler";

  }

}

onMounted(() => {
  socket.value = backendConnection.getWebSocket();

  const messageListener = (message: string) => {
    const message1: BackendMessage = JSON.parse(message);
    if (message1.message_type === MessageType.GET_ONLINE_GAME) {
      gameSession.value = message1.content.game;
      console.log(message1.content)
    }
    if (message1.message_type === MessageType.GAME_STATE) {
      console.log(message1.content)
      showGameState(message1.content)
    }
    if (message1.message_type === MessageType.ONLINE_PLAYER_HAND) {
      botCards.value = message1.content.cards;
      console.log(message1.content.cards)
    }
  };

  backendConnection.addMessageListener(messageListener);

  backendConnection.sendMessage(MessageType.GAME_START_READY);
  backendConnection.sendMessage(MessageType.GET_ONLINE_GAME);
});
</script>
<template>
  <div>
    <div v-for="message in messageFromServer" :key="message">{{ message }}</div>
    <div>
      <div class="flex gap-2 place-content-center">
        <div class="text-sm breadcrumbs">
          <ul>
            <li v-for="player in gameSession.players">
            <span
                :class="{'text-primary': gameState!.currentPlayer === player.playerName}"
                class="inline-flex gap-2 items-center">
              <i v-if="!player.isBot" class="bi bi-person"></i>
              <i v-else class="bi bi-robot"></i>
              <p>{{ player.playerName }}</p>
            </span>
            </li>
          </ul>
        </div>
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
