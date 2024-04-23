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
    JOIN_GAME = "JOIN_GAME",
    REQUEST_SERVER_CONNECTION = "REQUEST_SERVER_CONNECTION",
}

// Define the interface for an array of cards
export interface CardArray {
    cards: Card[];
}

export interface CardObject {
    card: Card;
}


// Define the interface for the game state
export interface GameState {
    gamePhase: GamePhase;
    currentPlayer?: number;
    card?: Card;
    color?: KartenFarbe;
    trumpf?: boolean;
}


export interface EmptyMessage {
    message_type: string;
    content: GameState | CardArray | CardObject;
}

// Define a union type for all possible message types
export type BackendMessage = EmptyMessage