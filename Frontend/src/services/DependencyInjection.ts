import {ServiceContainer} from "ioc-service-container";
import {BackendConnection} from "./BackendConnection.ts";

type IoCTypes = {
    BackendConnection: BackendConnection,
};

declare module 'ioc-service-container' {
    export function scg<T extends keyof IoCTypes, U extends IoCTypes[T]>(id: T): U;
}

export function setupService(backendUri: string) {
    const backendConnection = new BackendConnection(backendUri);
    ServiceContainer.set('BackendConnection', () => backendConnection);
}