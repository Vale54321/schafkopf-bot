<script lang="ts" setup>
import {onBeforeMount, ref, watch} from 'vue';
import {Card} from "../BackendMessage";

const props = defineProps<{
  card: Card;
}>();

const imgSrc = ref('/assets/card_back.jpg')

const focus = ref(false);

onBeforeMount(() => {
  imgSrc.value = "/assets/" + props.card.toString().toLowerCase() + ".jpg"
})

watch(() => props.card, (newCard) => {
  imgSrc.value = `/assets/${newCard.toString().toLowerCase()}.jpg`;
});
</script>
<template>
  <div
      :class="{'!scale-105 !z-10 !top-1/2 !left-1/2' : focus}" class="card transition  overflow-hidden"
      @click="focus=!focus">
    <img class="h-full rounded-[1rem] mx-auto" :src="imgSrc" alt="card">
  </div>
</template>

<style lang="scss">
.card {
  &.xl {
    @apply h-[48rem];
    img {
      @apply rounded-[2rem]
    }
  }

  &.md {
    @apply h-96;
    img {
      @apply rounded-2xl
    }
  }

  &.sm {
    @apply h-64;
    img {
      @apply rounded-xl
    }
  }
}
</style>
