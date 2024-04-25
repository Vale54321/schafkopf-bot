<script setup lang="ts">
import MessageComponent from "./MessageComponent.vue";
import {BackendMessage, MessageBoardType, MessageType} from "../BackendMessage.ts";
import {scg} from "ioc-service-container";
import {onMounted, ref} from "vue";

const backendConnection = scg("BackendConnection");
const socket = ref<WebSocket | null>();

const errorMessages = ref<{ message: string, type: MessageBoardType }[]>([]);

onMounted(() => {
  socket.value = backendConnection.getWebSocket();

  const messageListener = (message: string) => {
    const message1: BackendMessage = JSON.parse(message);
    if (message1.message_type === MessageType.UNKNOWN_ERROR && "error" in message1.content) {
      errorMessages.value.push({message: message1.content.error, type: MessageBoardType.ERROR});

      // Schedule removal for the newly added message
      setTimeout(() => {
        errorMessages.value.shift();
      }, 3000); // Adjust 3000 to your desired delay in milliseconds
    }

    if (message1.message_type === MessageType.INFO_MESSAGE && "message" in message1.content) {
      errorMessages.value.push({message: message1.content.message, type: MessageBoardType.INFO});

      // Schedule removal for the newly added message
      setTimeout(() => {
        errorMessages.value.shift();
      }, 3000); // Adjust 3000 to your desired delay in milliseconds
    }
  };

  backendConnection.addMessageListener(messageListener);
});

</script>

<template>
  <div class="fixed bottom-0 left-0 mx-auto px-8 py-2 w-screen flex flex-col gap-1">
    <MessageComponent v-for="message in errorMessages" :message="message.message" :type="message.type">
      message="Error! Task failed successfully." :type="MessageBoardType.WARNING">
    </MessageComponent>
  </div>
</template>

<style scoped lang="scss">

</style>