<script setup lang="ts">
import {onBeforeMount, ref} from "vue";
import {useRouter} from "vue-router";
import {scg} from "ioc-service-container";
import {BackendMessage, MessageType} from "../BackendMessage.ts";

const backendConnection = scg("BackendConnection");

const isConnected = ref<boolean>(false);
const isPingInProgress = ref<boolean>(false);
const secondsRemaining = ref<number>(10);

const checkInterval: number = 10;
const router = useRouter();

onBeforeMount(async () => {
  // Call checkConnection immediately and then every 10 seconds
  await checkConnectionWithCountdown();
});

async function checkConnectionWithCountdown() {
  await checkConnection();
  setTimeout(checkConnectionWithCountdown, checkInterval * 1000);
}

async function checkConnection(): Promise<void> {
  if (!isConnected.value) {
    isPingInProgress.value = true;
  }
  try {
    // Try to fetch a resource from the internet
    await fetch("http://10.6.9.57:8085/health", {mode: "no-cors"})
    // If successful, set isConnected to true
    isConnected.value = true;
  } catch (error) {
    // If an error occurs (e.g., network error), set isConnected to false
    isConnected.value = false;
  }
  isPingInProgress.value = false;

  let countDown = checkInterval;
  secondsRemaining.value = countDown;

  let countdownInterval = setTimeout(updateCountdown, 1000);

  function updateCountdown() {
    secondsRemaining.value = --countDown;
    if (countDown <= 0) clearInterval(countdownInterval);
    else countdownInterval = setTimeout(updateCountdown, 1000);
  }
}

async function openOnlineGameList() {
  backendConnection.sendMessage(MessageType.REQUEST_SERVER_CONNECTION);

  // Create a Promise<void> that resolves when the success message is received
  const successMessageReceived = new Promise<void>((resolve) => {
    const messageListener = (message: string) => {
      const message1: BackendMessage = JSON.parse(message);
      console.log(message)
      if (message1.message_type === "SERVER_CONNECTION_SUCCESSFUL") {
        // Resolve the Promise when the success message is received
        resolve();
      }
    };
    backendConnection.addMessageListener(messageListener);
  });


  // Wait for the success message to be received
  await successMessageReceived;

  // Once the success message is received, route to '/online'
  await router.push("/online");
}
</script>


<template>
  <div class="flex flex-col place-content-center h-screen gap-8 items-center">
    <div class="grid grid-cols-1 md:grid-cols-3 items-stretch px-2 gap-8">
      <div class="flex flex-col gap-6 bg-base-200 rounded-box p-8">
        <div class="flex flex-col gap-4 text-center">
          <h1 class="text-5xl font-bold">Lokales Spiel</h1>

          <span class="text-sm">mit NFC Reader</span>
        </div>

        <!-- Features -->
        <div class="flex flex-col">
          <div class="flex gap-2 items-center">
            <i class="bi bi-person text-accent"></i>
            für 3-4 reale Spieler
          </div>

          <div class="flex gap-2 items-center">
            <i class="bi bi-robot text-accent"></i>
            ggf. 1 virtueller Mitspieler
          </div>

          <div class="flex gap-2 mt-6 items-center">
            <i class="bi bi-exclamation-circle text-accent"></i>
            NFC Reader erforderlich
          </div>
        </div>

        <a class="btn btn-primary">Spielen</a>
      </div>

      <div class="flex flex-col gap-6 bg-base-200 rounded-box p-8">
        <div class="flex flex-col gap-4 text-center">
          <h1 class="text-5xl font-bold">Online</h1>

          <span class="text-sm">spiele gegen Spieler aus der ganzen Welt</span>
        </div>

        <!-- Features -->
        <div class="flex flex-col">
          <div class="flex gap-2 items-center">
            <i class="bi bi-person text-accent"></i>
            1 Spieler
          </div>

          <div class="flex gap-2 items-center">
            <i class="bi bi-pc-display text-accent"></i>
            2-3 online Mitspieler
          </div>

          <div class="flex gap-2 items-center">
            <i class="bi bi-robot text-accent"></i>
            ggf. 1-2 virtuelle Mitspieler
          </div>

          <div v-if="isPingInProgress" class="flex gap-2 mt-6 items-center">
            <i class="bi bi bi-arrow-repeat text-info animate-spin"></i>
            Serververbindung wird hergestellt ...
          </div>

          <div v-else-if="!isConnected" class="flex gap-2 mt-6 items-center">
            <i class="bi bi-x-lg text-error"></i>
            Serververbindung fehlgeschlagen ({{ secondsRemaining }} Sekunden)
          </div>

          <div v-else class="flex gap-2 mt-6 items-center">
            <i class="bi bi-check2 text-success"></i>
            Verbindung zum Server hergestellt
          </div>
        </div>

        <button :disabled="!isConnected" class="btn btn-primary" @click="openOnlineGameList()">Spielen</button>
      </div>

      <div class="flex flex-col gap-6 bg-base-200 rounded-box p-8">
        <div class="flex flex-col gap-4 text-center">
          <h1 class="text-5xl font-bold">Übung</h1>

          <span class="text-sm">spiele zum Üben gegen NPCs</span>
        </div>

        <!-- Features -->
        <div class="flex flex-col">
          <div class="flex gap-2 items-center">
            <i class="bi bi-person text-accent"></i>
            1 Spieler
          </div>

          <div class="flex gap-2 items-center">
            <i class="bi bi-robot text-accent"></i>
            3 virtuelle Mitspieler
          </div>
        </div>

        <a class="btn btn-primary">Spielen</a>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>