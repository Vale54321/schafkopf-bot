import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import {createRouter, createWebHistory} from "vue-router";
import {setupService} from "./services/DependencyInjection.ts";

const routes = [
    {path: '/', component: () => import('./pages/MainMenu.vue'),},
    {path: '/online', component: () => import('./pages/OnlineGameList.vue'),},
    {path: '/localgame', component: () => import('./pages/LocalGame.vue'),},
    {path: '/dedicatedgame', component: () => import('./pages/DedicatedGame.vue'),},
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

const websocketIp = import.meta.env.VITE_APP_WEBSOCKET_IP;
setupService("ws://" + websocketIp + ":8080/schafkopf-events/");

const app = createApp(App)
app.use(router)
app.mount('#app')
