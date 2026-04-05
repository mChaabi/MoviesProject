export interface User {
  id?: number;
  email: string;
  password?: string; // Optionnel car on ne le reçoit pas toujours en retour
  createdAt?: string;
}