<script setup lang="ts">
import {scg} from "ioc-service-container";
import {BackendMessage, GameSession, MessageType} from "../BackendMessage.ts";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const backendConnection = scg("BackendConnection");
const socket = ref<WebSocket | null>();

const gameList = ref<GameSession[]>([]);
const router = useRouter();

// Load playerName from localStorage or set default value if not present
const storedPlayerName = localStorage.getItem("playerName");
const playerName = ref<string>(storedPlayerName || "SchafkopfPlayer");

onMounted(() => {
  backendConnection.sendMessage(MessageType.SET_PLAYER_NAME, {playerName: playerName.value});
  refreshGameList();
  socket.value = backendConnection.getWebSocket();

  const messageListener = (message: string) => {
    const message1: BackendMessage = JSON.parse(message);
    if (message1.message_type === MessageType.LIST_ONLINE_GAMES && "games" in message1.content) {
      console.log(message1)
      gameList.value = message1.content.games;
    }
  };

  backendConnection.addMessageListener(messageListener);
});

function refreshGameList() {
  backendConnection.sendMessage(MessageType.LIST_ONLINE_GAMES);


}

async function createOnlineGame() {
  const serverName = `Schafkopf_${new Date().getTime()}`; // Append timestamp to server name
  backendConnection.sendMessage(MessageType.CREATE_ONLINE_GAME, {serverName: serverName});
  await router.push("/gamesession");
}

async function joinGame(serverName: string) {
  backendConnection.sendMessage(MessageType.JOIN_ONLINE_GAME, {serverName: serverName});
  await router.push("/gamesession");
}

// function getServerByName(serverName: string): GameSession | undefined {
//   return gameList.value.find(session => session.serverName === serverName);
// }

function sendPlayerName() {
  backendConnection.sendMessage(MessageType.SET_PLAYER_NAME, {playerName: playerName.value});
  localStorage.setItem("playerName", playerName.value);
}
</script>

<template>
  <router-link to="/">
    <button class="btn btn-primary">zurück</button>
  </router-link>

  <button class="btn btn-primary" @click="refreshGameList()">Refresh Game
    List
  </button>
  <input
      v-model="playerName" type="text" placeholder="Type here" class="input input-bordered w-full max-w-xs"
      @change="sendPlayerName()"/>
  <div class="flex flex-col max-w-xl mx-auto gap-2">
    <button class="btn btn-primary" @click="createOnlineGame()">Create Game</button>
    <div v-for="game in gameList" :key="game.serverName" class="flex max-w-lg">

      <div class="flex flex-col gap-4 p-6 bg-base-200 rounded-box">
        <h1 class="font-bold text-xl">{{ game.serverName }}</h1>

        <span>
            Lorem ipsum dolor sit amet consectetur adipisicing elit In odit
        </span>

        <div class="flex justify-between items-center">
          <span class="font-medium text-2xl">{{ game.playerCount }}/4</span>

          <a class="btn btn-primary btn-sm" @click="joinGame(game.serverName)">Join</a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>