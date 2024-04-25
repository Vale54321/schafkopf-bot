<script setup lang="ts">


import {MessageBoardType} from "../BackendMessage.ts";

defineProps<{
  message: string;
  type: MessageBoardType;
}>();

</script>

<template>
  <div role="alert" class="alert" :class="type">
    <i class="bi bi-exclamation"></i>
    <span>{{ message }}</span>
  </div>
</template>

<style scoped lang="scss">

</style>