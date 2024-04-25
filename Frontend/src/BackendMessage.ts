// Enum for KartenSymbol
enum KartenFarbe {
    SCHELL = "SCH",
    HERZ = "HERZ",
    BLATT = "BLATT",
    EICHEL = "EICHEL",
    TRUMPF = "TRUMPF",
}

// enum KartenSymbol {
//     SEVEN = "7",
//     EIGHT = "8",
//     NINE = "9",
//     TEN = "X",
//     UNTER = "U",
//     OBER = "O",
//     KOENIG = "K",
//     ASS = "A",
// }

export enum Card {
    EICHEL_7 = 'EICHEL_7',
    EICHEL_8 = 'EICHEL_8',
    EICHEL_9 = 'EICHEL_9',
    EICHEL_X = 'EICHEL_X',
    EICHEL_K = 'EICHEL_K',
    EICHEL_A = 'EICHEL_A',

    BLATT_7 = 'BLATT_7',
    BLATT_8 = 'BLATT_8',
    BLATT_9 = 'BLATT_9',
    BLATT_X = 'BLATT_X',
    BLATT_K = 'BLATT_K',
    BLATT_A = 'BLATT_A',

    SCHELL_7 = 'SCHELL_7',
    SCHELL_8 = 'SCHELL_8',
    SCHELL_9 = 'SCHELL_9',
    SCHELL_X = 'SCHELL_X',
    SCHELL_K = 'SCHELL_K',
    SCHELL_A = 'SCHELL_A',

    HERZ_7 = 'HERZ_7',
    HERZ_8 = 'HERZ_8',
    HERZ_9 = 'HERZ_9',
    HERZ_X = 'HERZ_X',
    HERZ_K = 'HERZ_K',
    HERZ_A = 'HERZ_A',

    SCHELL_U = 'SCHELL_U',
    HERZ_U = 'HERZ_U',
    BLATT_U = 'BLATT_U',
    EICHEL_U = 'EICHEL_U',

    SCHELL_O = 'SCHELL_O',
    HERZ_O = 'HERZ_O',
    BLATT_O = 'BLATT_O',
    EICHEL_O = 'EICHEL_O',

    BACK = "CARD_BACK"
    // Add other card combinations as needed
}

export enum GamePhase {
    CHOOSE_GAME = "CHOOSE_GAME",
    GAME_START = "GAME_START",
    GAME_STOP = "GAME_STOP",
    TRICK_START = "TRICK_START",
    WAIT_FOR_CARD = "WAIT_FOR_CARD",
    PLAYER_CARD = "PLAYER_CARD",
    PLAYER_TRICK = "PLAYER_TRICK"
}

export enum MessageType {
    PLAYER_CARD = "PLAYER_CARD",
    START_DEDICATED_GAME = "START_DEDICATED_GAME",
    JOIN_ONLINE_GAME = "JOIN_ONLINE_GAME",
    LEAVE_ONLINE_GAME = "LEAVE_ONLINE_GAME",
    REQUEST_SERVER_CONNECTION = "REQUEST_SERVER_CONNECTION",
    CREATE_ONLINE_GAME = "CREATE_ONLINE_GAME",
    LIST_ONLINE_GAMES = "LIST_ONLINE_GAMES",
    UNKNOWN_ERROR = "UNKNOWN_ERROR",
    INFO_MESSAGE = "INFO_MESSAGE",
    GET_ONLINE_GAME = "GET_ONLINE_GAME",
    SET_STATUS_READY = "SET_STATUS_READY",
    GAME_STATE = "GAME_STATE",
    ONLINE_PLAYER_HAND = "ONLINE_PLAYER_HAND",
    SERVER_CONNECTION_SUCCESSFUL = "SERVER_CONNECTION_SUCCESSFUL",
    SET_PLAYER_NAME = "SET_PLAYER_NAME",
    GAME_START_READY = "GAME_START_READY",
}

// Define the interface for an array of cards
export interface CardArrayMessage {
    message_type: MessageType.ONLINE_PLAYER_HAND;
    content: { cards: Card[] };
}

export interface CardMessage {
    message_type: MessageType.PLAYER_CARD;
    content: { card: Card };
}

// Define the interface for the game state
export interface GameState {
    gamePhase: GamePhase;
    currentPlayer?: string;
    card?: Card;
    color?: KartenFarbe;
    trumpf?: boolean;
}

export interface GameSession {
    serverName: string;
    playerCount: number;
    players: OnlinePlayer[];
}

export interface OnlinePlayer {
    playerName: string;
    isReady: boolean;
    isBot?: boolean;
}

export interface GameStateMessage {
    message_type: MessageType.GAME_STATE;
    content: GameState;
}

export interface GameListMessage {
    message_type: MessageType.LIST_ONLINE_GAMES;
    content: { games: GameSession[] };
}

export interface GameInfoMessage {
    message_type: MessageType.GET_ONLINE_GAME;
    content: { game: GameSession };
}

export interface JoinGameMessage {
    message_type: MessageType.JOIN_ONLINE_GAME;
    content: { serverName: string };
}

export interface ErrorMessage {
    message_type: MessageType.UNKNOWN_ERROR;
    content: { error: string };
}

export interface InfoMessage {
    message_type: MessageType.INFO_MESSAGE;
    content: { message: string };
}

export interface EmptyMessage {
    message_type: MessageType.SERVER_CONNECTION_SUCCESSFUL | MessageType.GAME_START_READY | MessageType.REQUEST_SERVER_CONNECTION;
}

export interface SetPlayerNameMessage {
    message_type: MessageType.SET_PLAYER_NAME;
    content: { playerName: string }
}

// Define a union type for all possible message types
export type BackendMessage =
    GameListMessage
    | JoinGameMessage
    | ErrorMessage
    | InfoMessage
    | GameInfoMessage | GameStateMessage | CardArrayMessage | CardMessage | EmptyMessage | SetPlayerNameMessage;

export enum MessageBoardType {
    ERROR = "alert-error",
    WARNING = "alert-warning",
    INFO = "alert-info",
    SUCCESS = "alert-success"
}