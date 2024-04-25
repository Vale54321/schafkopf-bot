<script setup lang="ts">
import {scg} from "ioc-service-container";
import {BackendMessage, GameSession, MessageType} from "../BackendMessage.ts";
import {computed, onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const backendConnection = scg("BackendConnection");
const socket = ref<WebSocket | null>();

const gameSession = ref<GameSession>({
  serverName: "",
  playerCount: 0,
  players: []
});

const router = useRouter();

const allPlayersReady = computed(() => {
  // Check if all players are ready by iterating through them
  return gameSession.value.players.every(player => player.isReady);
});

onMounted(() => {
  refreshGameInfo();
  socket.value = backendConnection.getWebSocket();


  const messageListener = async (message: string) => {
    const message1: BackendMessage = JSON.parse(message);
    console.log(message1)
    if (message1.message_type === MessageType.GET_ONLINE_GAME && "game" in message1.content) {
      gameSession.value = message1.content.game;
    }
    if (message1.message_type === MessageType.GAME_START_READY) {
      // Resolve the Promise when the success message is received
      await router.push("/dedicatedgame");
    }
  };

  backendConnection.addMessageListener(messageListener);


});

function refreshGameInfo() {
  backendConnection.sendMessage(MessageType.GET_ONLINE_GAME);
}

async function leaveGame() {
  backendConnection.sendMessage(MessageType.LEAVE_ONLINE_GAME);
  await router.push("/online");
}


function setStatusReady() {
  backendConnection.sendMessage(MessageType.SET_STATUS_READY);
}

async function startDedicated() {
  backendConnection.sendMessage(MessageType.START_DEDICATED_GAME);

  const successMessageReceived = new Promise<void>((resolve) => {
    const messageListener = (message: string) => {
      const message1: BackendMessage = JSON.parse(message);
      console.log(message)
      if (message1.message_type === MessageType.GAME_START_READY) {
        // Resolve the Promise when the success message is received
        resolve();
      }
    };
    backendConnection.addMessageListener(messageListener);
  });

  await successMessageReceived;

  await router.push("/dedicatedgame");
}
</script>

<template>
  <button class="btn btn-primary" @click="leaveGame()">Leave Game
  </button>

  <div class="flex flex-col gap-2 max-w-xl mx-auto">
    <div class="flex flex-col gap-4 p-6 bg-base-200 rounded-box">
      <h1 class="font-bold text-xl">{{ gameSession.serverName }}</h1>

      <span>
            Lorem ipsum dolor sit amet consectetur adipisicing elit In odit
        </span>

      <div class="flex justify-between items-center">
        <span class="font-medium text-2xl">{{ gameSession.playerCount }}/4</span>

      </div>
    </div>
    Spieler:
    <div v-for="player in gameSession.players" class="flex flex-row gap-4 p-6 bg-base-200 rounded-box">
      <p class="grow">{{ player.playerName }}</p>
      <p v-if="player.isBot" class="text-warning"><i class="bi bi-robot"></i></p>
      <p v-else-if="player.isReady" class="text-success">Bereit</p>
      <p v-else class="text-error">Nicht bereit</p>
    </div>
    <button class="btn btn-primary" @click="setStatusReady()">Toggle Ready
    </button>
    <button :disabled="!allPlayersReady" class="btn btn-primary max-w-xl" @click="startDedicated()">Starten</button>
  </div>
</template>

<style scoped lang="scss">

</style>