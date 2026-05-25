export type User = {
  id: number;
  email: string;
  fullName: string;
  avatarUrl?: string | null;
};

type AuthState = {
  token: string | null;
  user: User | null;
};

const STORAGE_KEY = 'memoreel-auth';

export const authStore = {
  get(): AuthState {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return { token: null, user: null };
    }

    try {
      return JSON.parse(raw) as AuthState;
    } catch {
      return { token: null, user: null };
    }
  },

  set(state: AuthState) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  },

  clear() {
    localStorage.removeItem(STORAGE_KEY);
  }
};

// Made with Bob
