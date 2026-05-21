import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './store/authStore';

// Layouts
import AuthLayout from './layouts/AuthLayout';
import DashboardLayout from './layouts/DashboardLayout';

// Auth Pages
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';

// Dashboard Pages
import DashboardPage from './pages/dashboard/DashboardPage';
import MyProjectsPage from './pages/dashboard/MyProjectsPage';
import MemorialsPage from './pages/dashboard/MemorialsPage';
import SettingsPage from './pages/dashboard/SettingsPage';

// Project Pages
import CreateProjectPage from './pages/project/CreateProjectPage';
import ProjectDetailsPage from './pages/project/ProjectDetailsPage';
import UploadMediaPage from './pages/project/UploadMediaPage';
import PreviewPage from './pages/project/PreviewPage';

// Public Pages
import PublicViewerPage from './pages/public/PublicViewerPage';

function App() {
  const { isAuthenticated } = useAuthStore();

  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/m/:slug" element={<PublicViewerPage />} />

      {/* Auth Routes */}
      <Route element={<AuthLayout />}>
        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to="/dashboard" replace /> : <LoginPage />}
        />
        <Route
          path="/register"
          element={isAuthenticated ? <Navigate to="/dashboard" replace /> : <RegisterPage />}
        />
      </Route>

      {/* Protected Dashboard Routes */}
      <Route
        element={
          isAuthenticated ? <DashboardLayout /> : <Navigate to="/login" replace />
        }
      >
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/projects" element={<MyProjectsPage />} />
        <Route path="/memorials" element={<MemorialsPage />} />
        <Route path="/settings" element={<SettingsPage />} />
        
        {/* Project Creation & Management */}
        <Route path="/projects/create" element={<CreateProjectPage />} />
        <Route path="/projects/:id" element={<ProjectDetailsPage />} />
        <Route path="/projects/:id/upload" element={<UploadMediaPage />} />
        <Route path="/projects/:id/preview" element={<PreviewPage />} />
      </Route>

      {/* Default Route */}
      <Route
        path="/"
        element={
          isAuthenticated ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />
        }
      />

      {/* 404 Route */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;

// Made with Bob
