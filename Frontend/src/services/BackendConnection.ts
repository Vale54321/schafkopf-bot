import {MessageType} from "../BackendMessage.ts";

interface JsonMessage {
    origin: string;
    message: any; // Adjust 'any' type as per your expected message structure
}

export class BackendConnection {


    private readonly webSocket: WebSocket;
    private messageListeners: ((message: string) => void)[] = [];
    private backendUri: string;


    constructor(backendUri: string) {
        this.backendUri = backendUri;

        this.webSocket = new WebSocket(backendUri);

        // Registering event listener for message reception
        this.webSocket.addEventListener('message', this.handleMessage.bind(this));
        // Handle connection closed
        this.webSocket.addEventListener("close", (event) => {
            console.log("WebSocket connection closed:", event);
        });

        // Handle errors
        this.webSocket.addEventListener("error", (event) => {
            console.error("WebSocket error:", event);
        });
    }

    public sendMessage(messageType: MessageType, message?: any): void {
        let jsonMessage;
        if (message === undefined) {
            jsonMessage = {
                origin: "FRONTEND",
                message: {
                    message_type: messageType,
                }
            };
        } else {
            jsonMessage = {
                origin: "FRONTEND",
                message: {
                    message_type: messageType,
                    content: message
                }
            };
        }

        console.log("Sending message:", jsonMessage);
        this.webSocket.send(JSON.stringify(jsonMessage));
    }

    public getWebSocket(): WebSocket {
        return this.webSocket;
    }

    public addMessageListener(listener: (message: string) => void): void {
        this.messageListeners.push(listener);
    }

    public removeMessageListener(listener: (message: string) => void): void {
        this.messageListeners = this.messageListeners.filter(l => l !== listener);
    }

    private handleMessage(event: MessageEvent): void {
        const message = event.data as string;
        // Notify all registered message listeners
        this.messageListeners.forEach(listener => {
            listener(message);
        });
    }
}