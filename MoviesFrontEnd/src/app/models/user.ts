export interface User {
  id?: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER'; // <-- Ajoute cette ligne
  createdAt?: string;
}
