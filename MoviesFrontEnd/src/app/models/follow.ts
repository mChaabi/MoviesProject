export interface Follow {
    id?: number;
    followerId: number;   // ID de celui qui suit
    followingId: number;  // ID de celui qui est suivi
    createdAt?: Date;     // Optionnel, pour afficher "Abonné depuis le..."
}
