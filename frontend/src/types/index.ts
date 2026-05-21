/**
 * Memoreel Frontend Type Definitions
 * 
 * These types match the backend API contracts and domain models.
 */

// ============================================
// ENUMS
// ============================================

export type ProjectStatus = 'draft' | 'processing' | 'completed' | 'published' | 'failed';

export type OccasionType = 
  | 'birthday' 
  | 'anniversary' 
  | 'wedding' 
  | 'graduation' 
  | 'baby_shower' 
  | 'housewarming' 
  | 'retirement'
  | 'farewell' 
  | 'trip_memory' 
  | 'friendship' 
  | 'reunion' 
  | 'milestone' 
  | 'achievement'
  | 'memorial' 
  | 'condolence' 
  | 'remembrance_day'
  | 'custom';

export type Privacy = 'public' | 'friends' | 'private';

export type MediaType = 'image' | 'video';

export type VideoJobStatus = 'queued' | 'processing' | 'completed' | 'failed';

// ============================================
// USER TYPES
// ============================================

export interface User {
  id: string;
  email: string;
  avatarUrl?: string;
  emailVerified: boolean;
  provider?: string;
  createdAt: Date;
}

export interface AuthResponse {
  token: string;
  user: User;
}

// ============================================
// PROJECT TYPES
// ============================================

export interface Project {
  id: string;
  userId: string;
  title: string;
  type: OccasionType;
  status: ProjectStatus;
  privacy: Privacy;
  metadata: Record<string, any>;
  createdAt: Date;
  updatedAt: Date;
  publishedAt?: Date;
}

export interface CreateProjectRequest {
  title: string;
  type: OccasionType;
  metadata: Record<string, any>;
}

export interface UpdateProjectRequest {
  title?: string;
  metadata?: Record<string, any>;
}

// ============================================
// MEMORIAL TYPES
// ============================================

export interface MemorialMetadata {
  personName: string;
  birthDate?: string;
  deathDate: string;
  description: string;
  profileImage?: string;
  relationship?: string;
  lifeStory?: string;
}

// ============================================
// MEDIA TYPES
// ============================================

export interface Media {
  id: string;
  projectId: string;
  type: MediaType;
  url: string;
  thumbnailUrl?: string;
  orderIndex: number;
  size: number;
  mimeType?: string;
  originalFilename?: string;
  uploadedAt: Date;
}

export interface MediaUploadResponse {
  id: string;
  projectId: string;
  type: MediaType;
  url: string;
  thumbnail: string;
  orderIndex: number;
  size: number;
  uploadedAt: string;
}

// ============================================
// VIDEO TYPES
// ============================================

export interface Video {
  url: string;
  duration: number;
  resolution: string;
  size: number;
  createdAt: Date;
}

export interface VideoJob {
  jobId: string;
  projectId: string;
  status: VideoJobStatus;
  progress?: number;
  startedAt?: Date;
  estimatedCompletion?: Date;
}

export interface GenerateVideoRequest {
  projectId: string;
  templateId?: string;
}

// ============================================
// PUBLIC LINK & QR CODE TYPES
// ============================================

export interface PublicLink {
  projectId: string;
  publicUrl: string;
  slug: string;
  qrCodeUrl: string;
  publishedAt: Date;
}

export interface QRCode {
  qrCodeUrl: string;
  publicUrl: string;
  downloadUrl: string;
  size: string;
  format: string;
}

// ============================================
// API RESPONSE TYPES
// ============================================

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
}

export interface ApiError {
  success: false;
  error: {
    code: string;
    message: string;
    details?: Record<string, any>;
  };
}

export interface PaginatedResponse<T> {
  items: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

// ============================================
// FORM TYPES
// ============================================

export interface LoginFormData {
  email: string;
  password: string;
}

export interface RegisterFormData {
  email: string;
  password: string;
}

export interface ProjectFormData {
  title: string;
  type: OccasionType;
  metadata: Record<string, any>;
}

// ============================================
// DASHBOARD TYPES
// ============================================

export interface DashboardStats {
  totalProjects: number;
  completedProjects: number;
  publishedProjects: number;
}

export interface UserStats {
  totalProjects: number;
  completedProjects: number;
  publishedProjects: number;
  totalViews: number;
  storageUsed: number;
  storageLimit: number;
}

// Made with Bob
