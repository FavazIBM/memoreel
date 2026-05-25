import { useMemo, useState } from 'react';
import { authStore } from './store/authStore';

const API_BASE_URL = 'http://localhost:8080';

type ApiEnvelope<T> = {
  success: boolean;
  data: T;
  message?: string;
};

type BackendProjectDetail = {
  id: number;
  title: string;
  type: string;
  status: ProjectStatus;
  privacy: 'public';
  metadata: Partial<WizardForm>;
  media: Array<{
    id: number;
    type: 'image' | 'video';
    url: string;
    thumbnailUrl?: string | null;
    orderIndex: number;
    originalFilename?: string;
  }>;
  video?: {
    url: string;
    duration: number;
    createdAt: string;
  } | null;
  videoJob?: {
    id: number;
    status: 'queued' | 'processing' | 'completed' | 'failed';
    startedAt?: string;
    completedAt?: string;
    errorMessage?: string;
  } | null;
  publicLink?: {
    slug: string;
    url: string;
  } | null;
  qrCode?: {
    url: string;
    publicUrl?: string;
  } | null;
  createdAt: string;
  updatedAt: string;
  publishedAt?: string | null;
};

type ProjectStatus = 'draft' | 'processing' | 'completed' | 'published' | 'failed';
type Screen = 'auth' | 'dashboard' | 'projects' | 'memorialCreate' | 'wizard' | 'publicView' | 'settings';
type WizardStep = 1 | 2 | 3 | 4 | 5 | 6 | 7;
type AuthView = 'login' | 'register';
type WizardMode = 'create' | 'edit';

type OccasionCard = {
  key: string;
  label: string;
  subtitle: string;
  emoji: string;
};

type WizardForm = {
  title: string;
  type: string;
  personName: string;
  relationship: string;
  datePrimary: string;
  dateSecondary: string;
  yearsTogether: string;
  venue: string;
  profileImage: string;
  description: string;
  specialMessage: string;
  caption: string;
};

type MediaItem = {
  id: number;
  orderIndex: number;
  kind: 'image' | 'video';
  tone: number;
  name?: string;
  previewUrl?: string;
  generated?: boolean;
};

type Project = {
  id: number;
  title: string;
  type: string;
  category: string;
  status: ProjectStatus;
  privacy: 'public';
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
  metadata: WizardForm;
  media: MediaItem[];
  videoUrl?: string;
  publicSlug?: string;
  qrUrl?: string;
};

const occasions: OccasionCard[] = [
  { key: 'birthday', label: 'Birthday', subtitle: 'Celebrate a special day', emoji: '🎂' },
  { key: 'anniversary', label: 'Anniversary', subtitle: 'Relive the memories', emoji: '🌸' },
  { key: 'wedding', label: 'Wedding', subtitle: 'Cherish the big day', emoji: '💍' },
  { key: 'graduation', label: 'Graduation', subtitle: 'Mark a milestone', emoji: '🎓' },
  { key: 'baby_shower', label: 'Baby Shower', subtitle: 'Welcome new life', emoji: '👶' },
  { key: 'housewarming', label: 'Housewarming', subtitle: 'Celebrate a new home', emoji: '🏡' },
  { key: 'retirement', label: 'Retirement', subtitle: 'Honor a career', emoji: '🎉' },
  { key: 'farewell', label: 'Farewell', subtitle: 'Say goodbye with love', emoji: '👋' },
  { key: 'trip_memory', label: 'Trip Memory', subtitle: 'Relive your journey', emoji: '🛫' },
  { key: 'friendship', label: 'Friendship', subtitle: 'Celebrate a bond', emoji: '🤝' },
  { key: 'reunion', label: 'Reunion', subtitle: 'Reconnect with joy', emoji: '🥂' },
  { key: 'milestone', label: 'Milestone', subtitle: 'Mark a big moment', emoji: '🏆' },
  { key: 'achievement', label: 'Achievement', subtitle: 'Celebrate success', emoji: '✨' },
  { key: 'memorial', label: 'Memorial', subtitle: 'Honor a loved one', emoji: '🕊️' },
  { key: 'condolence', label: 'Condolence', subtitle: 'Share remembrance', emoji: '🤍' },
  { key: 'remembrance_day', label: 'Remembrance Day', subtitle: 'Keep memories alive', emoji: '🕯️' },
  { key: 'custom', label: 'Custom', subtitle: 'Your own occasion', emoji: '✦' }
];

const defaultForm: WizardForm = {
  title: "Mom's 60th Birthday Party",
  type: 'birthday',
  personName: 'Mom',
  relationship: 'Mother',
  datePrimary: '2026-04-05',
  dateSecondary: '',
  yearsTogether: '',
  venue: '',
  profileImage: '',
  description: "To the most amazing mom — 60 years of love, laughter, and unforgettable memories.",
  specialMessage: "Here's to you! 🎉",
  caption: "To the most amazing mom — 60 years of love, laughter, and unforgettable memories. Here's to you! 🎉"
};

const starterProjects: Project[] = [
  {
    id: 1,
    title: "Mom's 60th Birthday 🎉",
    type: 'birthday',
    category: 'Celebration',
    status: 'published',
    privacy: 'public',
    createdAt: '2026-04-01T09:00:00Z',
    updatedAt: '2026-04-08T11:30:00Z',
    publishedAt: '2026-04-08T11:30:00Z',
    metadata: {
      ...defaultForm,
      title: "Mom's 60th Birthday 🎉",
      type: 'birthday',
      personName: 'Mom',
      relationship: 'Mother',
      specialMessage: "Family trips to birthday surprises — every moment is a testament to the joy you bring into our lives.",
      caption: "Family trips to birthday surprises — every moment captured here is a testament to the joy you bring into our lives."
    },
    media: Array.from({ length: 8 }, (_, index) => ({
      id: index + 1,
      orderIndex: index + 1,
      kind: 'image',
      tone: index % 6
    })),
    videoUrl: 'https://example.com/videos/birthday.mp4',
    publicSlug: 'abc123xyz',
    qrUrl: 'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://memoreel.app/m/abc123xyz'
  },
  {
    id: 2,
    title: 'In Memory of Grandpa',
    type: 'memorial',
    category: 'Memorial',
    status: 'published',
    privacy: 'public',
    createdAt: '2026-04-10T09:00:00Z',
    updatedAt: '2026-04-14T11:30:00Z',
    publishedAt: '2026-04-14T11:30:00Z',
    metadata: {
      ...defaultForm,
      title: 'In Memory of Grandpa',
      type: 'memorial',
      personName: 'Grandpa Joseph',
      relationship: 'Grandfather',
      datePrimary: '1940-03-10',
      dateSecondary: '2026-03-02',
      description: 'A gentle tribute filled with stories, warmth, and cherished family memories.',
      specialMessage: 'Your kindness and wisdom continue to guide us.',
      caption: 'A calm and respectful memorial reel created to celebrate a beautiful life.'
    },
    media: Array.from({ length: 6 }, (_, index) => ({
      id: 100 + index,
      orderIndex: index + 1,
      kind: 'image',
      tone: (index + 2) % 6
    })),
    videoUrl: 'https://example.com/videos/memorial.mp4',
    publicSlug: 'memorial8ab',
    qrUrl: 'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://memoreel.app/m/memorial8ab'
  },
  {
    id: 3,
    title: 'Wedding Anniversary ♡',
    type: 'anniversary',
    category: 'Celebration',
    status: 'draft',
    privacy: 'public',
    createdAt: '2026-05-05T09:00:00Z',
    updatedAt: '2026-05-05T09:00:00Z',
    metadata: {
      ...defaultForm,
      title: 'Wedding Anniversary ♡',
      type: 'anniversary',
      personName: 'Ava & Noah',
      relationship: 'Family',
      datePrimary: '2026-05-25',
      yearsTogether: '10',
      description: 'A heartfelt reel for a beautiful journey together.',
      specialMessage: 'Celebrating ten years of memories, laughter, and love.',
      caption: 'A reel prepared for a special anniversary celebration.'
    },
    media: [],
    publicSlug: undefined,
    qrUrl: undefined
  }
];

function getCategory(type: string) {
  if (['birthday', 'anniversary', 'wedding', 'graduation', 'baby_shower', 'housewarming', 'retirement'].includes(type)) {
    return 'Celebration';
  }
  if (['memorial', 'condolence', 'remembrance_day'].includes(type)) {
    return 'Memorial';
  }
  if (['farewell', 'trip_memory', 'friendship', 'reunion', 'milestone', 'achievement'].includes(type)) {
    return 'Social';
  }
  return 'Custom';
}

function createMockMedia(count: number): MediaItem[] {
  return Array.from({ length: count }, (_, index) => ({
    id: Date.now() + index,
    orderIndex: index + 1,
    kind: index % 5 === 0 ? 'video' : 'image',
    tone: index % 6
  }));
}

function App() {
  const savedAuth = authStore.get();
  const [screen, setScreen] = useState<Screen>(savedAuth.user ? 'dashboard' : 'auth');
  const [authView, setAuthView] = useState<AuthView>('login');
  const [wizardMode, setWizardMode] = useState<WizardMode>('create');
  const [wizardStep, setWizardStep] = useState<WizardStep>(1);
  const [auth, setAuth] = useState(savedAuth);
  const [projects, setProjects] = useState<Project[]>(starterProjects);
  const [activeProjectId, setActiveProjectId] = useState<number>(starterProjects[0].id);
  const [form, setForm] = useState<WizardForm>(defaultForm);
  const [generationCount, setGenerationCount] = useState(1);
  const [selectedMediaIds, setSelectedMediaIds] = useState<number[]>([]);
  const [isReorderMode, setIsReorderMode] = useState(false);
  const [settingsPhoto, setSettingsPhoto] = useState<string | null>(null);
  const [passwordValue, setPasswordValue] = useState('password');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const activeProject =
    projects.find((project) => project.id === activeProjectId) ?? projects[0];

  const stats = useMemo(
    () => ({
      totalProjects: projects.length,
      completedProjects: projects.filter((project: Project) => project.status === 'completed').length,
      publishedProjects: projects.filter((project: Project) => project.status === 'published').length
    }),
    [projects]
  );

  const getPublicVideoLink = (project?: Project) =>
    project?.publicSlug ? `${API_BASE_URL}/public/${project.publicSlug}` : '';

  const mapBackendProjectToProject = (backendProject: BackendProjectDetail): Project => {
    const publicUrl = backendProject.publicLink?.url || (backendProject.publicLink?.slug ? `${API_BASE_URL}/public/${backendProject.publicLink.slug}` : '');
    const qrUrl =
      publicUrl.length > 0
        ? `https://api.qrserver.com/v1/create-qr-code/?size=512x512&data=${encodeURIComponent(publicUrl)}`
        : undefined;

    return {
      id: backendProject.id,
      title: backendProject.title,
      type: backendProject.type,
      category: getCategory(backendProject.type),
      status: backendProject.status,
      privacy: 'public',
      createdAt: backendProject.createdAt,
      updatedAt: backendProject.updatedAt,
      publishedAt: backendProject.publishedAt ?? undefined,
      metadata: {
        ...defaultForm,
        ...backendProject.metadata,
        title: backendProject.title,
        type: backendProject.type
      },
      media: (backendProject.media ?? []).map((item, index) => ({
        id: item.id,
        orderIndex: item.orderIndex ?? index + 1,
        kind: item.type,
        tone: index % 6,
        name: item.originalFilename,
        previewUrl: item.url.startsWith('http') ? item.url : `${API_BASE_URL}${item.url}`,
        generated: backendProject.status === 'completed' || backendProject.status === 'published'
      })),
      videoUrl: backendProject.video?.url ? `${API_BASE_URL}${backendProject.video.url}` : undefined,
      publicSlug: backendProject.publicLink?.slug ?? undefined,
      qrUrl
    };
  };

  const saveAuth = (token: string, email: string, fullName: string) => {
    const next = {
      token,
      user: {
        id: 1,
        email,
        fullName,
        avatarUrl: null
      }
    };
    setAuth(next);
    authStore.set(next);
    setScreen('dashboard');
  };

  const signOut = () => {
    authStore.clear();
    setAuth({ token: null, user: null });
    setScreen('auth');
  };

  const openCreateWizard = (type?: string) => {
    const nextType = type ?? 'birthday';
    setWizardMode('create');
    setWizardStep(1);
    setGenerationCount(1);
    setForm({
      ...defaultForm,
      type: nextType,
      title: nextType === 'memorial' ? 'In Memory of Grandpa' : "Mom's 60th Birthday Party",
      personName: nextType === 'memorial' ? 'Grandpa Joseph' : 'Mom',
      relationship: nextType === 'memorial' ? 'Grandfather' : 'Mother',
      datePrimary: nextType === 'memorial' ? '1940-03-10' : '2026-04-05',
      dateSecondary: nextType === 'memorial' ? '2026-03-02' : '',
      description:
        nextType === 'memorial'
          ? 'A gentle tribute filled with stories, warmth, and cherished family memories.'
          : defaultForm.description,
      specialMessage:
        nextType === 'memorial'
          ? 'Your kindness and wisdom continue to guide us.'
          : defaultForm.specialMessage,
      caption:
        nextType === 'memorial'
          ? 'A calm and respectful memorial reel created to celebrate a beautiful life.'
          : defaultForm.caption
    });
    setScreen(nextType === 'memorial' ? 'memorialCreate' : 'wizard');
  };

  const openDraftProject = (project: Project) => {
    setActiveProjectId(project.id);
    setWizardMode('edit');
    setWizardStep(stepForStatus(project.status));
    setForm(project.metadata);
    setScreen(project.status === 'published' ? 'publicView' : 'wizard');
  };

  const stepForStatus = (status: ProjectStatus): WizardStep => {
    if (status === 'draft') {
      return 1;
    }
    if (status === 'processing') {
      return 4;
    }
    if (status === 'completed') {
      return 5;
    }
    if (status === 'published') {
      return 7;
    }
    return 4;
  };

  const ensureDraftProject = async () => {
    if (wizardMode === 'edit' && activeProject && activeProject.id < 1000000000000) {
      return activeProject.id;
    }

    const response = await fetch(`${API_BASE_URL}/projects`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-User-Id': String(auth.user?.id ?? 1)
      },
      body: JSON.stringify({
        title: form.title,
        type: form.type,
        metadata: form
      })
    });

    const result = (await response.json()) as ApiEnvelope<{ id: number; title: string; type: string; status: ProjectStatus; createdAt: string }>;
    if (!response.ok || !result.success) {
      throw new Error(result.message || 'Failed to create project');
    }

    const backendProjectId = result.data.id;
    const now = new Date().toISOString();
    const project: Project = {
      id: backendProjectId,
      title: form.title,
      type: form.type,
      category: getCategory(form.type),
      status: 'draft',
      privacy: 'public',
      createdAt: result.data.createdAt ?? now,
      updatedAt: now,
      metadata: form,
      media: []
    };

    setProjects((current: Project[]) => {
      const exists = current.some((item: Project) => item.id === backendProjectId);
      return exists ? current : [project, ...current.filter((item: Project) => item.id !== activeProjectId)];
    });
    setActiveProjectId(backendProjectId);
    setWizardMode('edit');
    return backendProjectId;
  };

  const updateProject = (projectId: number, updater: (project: Project) => Project) => {
    setProjects((current: Project[]) =>
      current.map((project: Project) => (project.id === projectId ? updater(project) : project))
    );
  };

  const deleteProject = (projectId: number) => {
    setProjects((current: Project[]) => current.filter((project: Project) => project.id !== projectId));

    if (activeProjectId === projectId) {
      const remainingProjects = projects.filter((project: Project) => project.id !== projectId);
      if (remainingProjects.length > 0) {
        setActiveProjectId(remainingProjects[0].id);
      }
    }
  };

  const uploadMediaFiles = async (files: FileList | null) => {
    if (!files || files.length === 0 || isSubmitting) {
      return;
    }

    const projectId = await ensureDraftProject();
    updateProject(projectId, (project: Project) => ({
      ...project,
      title: form.title,
      type: form.type,
      category: getCategory(form.type),
      metadata: form,
      updatedAt: new Date().toISOString()
    }));

    const payload = new FormData();
    Array.from(files).forEach((file: File) => payload.append('files', file));

    setIsSubmitting(true);
    try {
      const response = await fetch(`${API_BASE_URL}/projects/${projectId}/media`, {
        method: 'POST',
        headers: {
          'X-User-Id': String(auth.user?.id ?? 1)
        },
        body: payload
      });

      const result = (await response.json()) as ApiEnvelope<BackendProjectDetail>;
      if (!response.ok || !result.success) {
        throw new Error(result.message || 'Failed to upload media');
      }

      const mappedProject = mapBackendProjectToProject(result.data);
      setProjects((current: Project[]) =>
        current.map((project: Project) => (project.id === projectId ? { ...mappedProject, metadata: form } : project))
      );
      setSelectedMediaIds([]);
    } catch (error) {
      window.alert(error instanceof Error ? error.message : 'Failed to upload media');
    } finally {
      setIsSubmitting(false);
    }
  };

  const moveMediaItem = (fromIndex: number, toIndex: number) => {
    if (!currentProject) {
      return;
    }

    const projectId = currentProject.id;
    updateProject(projectId, (project: Project) => {
      if (
        fromIndex < 0 ||
        toIndex < 0 ||
        fromIndex >= project.media.length ||
        toIndex >= project.media.length ||
        fromIndex === toIndex
      ) {
        return project;
      }

      const reordered = [...project.media];
      const [movedItem] = reordered.splice(fromIndex, 1);
      reordered.splice(toIndex, 0, movedItem);

      return {
        ...project,
        media: reordered.map((item: MediaItem, index: number) => ({
          ...item,
          orderIndex: index + 1
        })),
        updatedAt: new Date().toISOString()
      };
    });
  };

  const toggleSelectAllMedia = () => {
    if (selectedMediaIds.length === currentMedia.length) {
      setSelectedMediaIds([]);
      return;
    }

    setSelectedMediaIds(currentMedia.map((item: MediaItem) => item.id));
  };

  const toggleMediaSelection = (mediaId: number) => {
    setSelectedMediaIds((current: number[]) =>
      current.includes(mediaId) ? current.filter((id: number) => id !== mediaId) : [...current, mediaId]
    );
  };

  const removeSelectedMedia = () => {
    if (selectedMediaIds.length === 0 || !currentProject) {
      return;
    }

    const projectId = currentProject.id;
    updateProject(projectId, (project: Project) => ({
      ...project,
      media: project.media
        .filter((item: MediaItem) => !selectedMediaIds.includes(item.id))
        .map((item: MediaItem, index: number) => ({
          ...item,
          orderIndex: index + 1
        })),
      updatedAt: new Date().toISOString()
    }));
    setSelectedMediaIds([]);
  };

  const handleWizardNext = async () => {
    if (isSubmitting) {
      return;
    }

    if (wizardStep === 1) {
      setWizardStep(2);
      return;
    }

    if (wizardStep === 2) {
      const projectId = await ensureDraftProject();
      updateProject(projectId, (project: Project) => ({
        ...project,
        title: form.title,
        type: form.type,
        category: getCategory(form.type),
        metadata: form,
        updatedAt: new Date().toISOString()
      }));
      setWizardStep(3);
      return;
    }

    if (wizardStep === 3) {
      const projectId = await ensureDraftProject();
      updateProject(projectId, (project: Project) => ({
        ...project,
        media: project.media,
        metadata: form,
        updatedAt: new Date().toISOString()
      }));
      setWizardStep(4);
      return;
    }

    if (wizardStep === 4 || wizardStep === 6) {
      const projectId = await ensureDraftProject();
      setIsSubmitting(true);
      try {
        const response = await fetch(`${API_BASE_URL}/projects/${projectId}/generate`, {
          method: 'POST',
          headers: {
            'X-User-Id': String(auth.user?.id ?? 1)
          }
        });

        const result = (await response.json()) as ApiEnvelope<BackendProjectDetail>;
        if (!response.ok || !result.success) {
          throw new Error(result.message || 'Failed to generate video');
        }

        const mappedProject = mapBackendProjectToProject(result.data);
        setProjects((current: Project[]) =>
          current.map((project: Project) => (project.id === projectId ? { ...mappedProject, metadata: form } : project))
        );
        setGenerationCount((count: number) => count + 1);
        setWizardStep(5);
      } catch (error) {
        window.alert(error instanceof Error ? error.message : 'Failed to generate video');
      } finally {
        setIsSubmitting(false);
      }
      return;
    }

    if (wizardStep === 5) {
      setWizardStep(6);
      return;
    }

    if (wizardStep === 7) {
      setScreen('dashboard');
    }
  };

  const handleWizardBack = () => {
    if (wizardStep === 1) {
      setScreen('dashboard');
      return;
    }
    if (wizardStep === 5) {
      setWizardStep(4);
      return;
    }
    if (wizardStep === 7) {
      setWizardStep(5);
      return;
    }
    setWizardStep((current) => Math.max(1, current - 1) as WizardStep);
  };

  const publishProject = async () => {
    if (isSubmitting) {
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await fetch(`${API_BASE_URL}/projects/${activeProjectId}/publish`, {
        method: 'POST',
        headers: {
          'X-User-Id': String(auth.user?.id ?? 1)
        }
      });

      const result = (await response.json()) as ApiEnvelope<BackendProjectDetail>;
      if (!response.ok || !result.success) {
        throw new Error(result.message || 'Failed to publish project');
      }

      const mappedProject = mapBackendProjectToProject(result.data);
      setProjects((current: Project[]) =>
        current.map((project: Project) => (project.id === activeProjectId ? { ...mappedProject, metadata: form } : project))
      );
      setWizardStep(7);
      setScreen('wizard');
    } catch (error) {
      window.alert(error instanceof Error ? error.message : 'Failed to publish project');
    } finally {
      setIsSubmitting(false);
    }
  };

  const currentProject =
    projects.find((project) => project.id === activeProjectId) ?? activeProject;
  const currentMedia = currentProject?.media ?? [];

  const renderOccasionFields = () => {
    if (form.type === 'memorial' || form.type === 'condolence' || form.type === 'remembrance_day') {
      return (
        <>
          <label className="mm-detail-field">
            <span>Deceased person's name</span>
            <input
              value={form.personName}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, personName: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Birth date (optional)</span>
            <input
              type="date"
              value={form.datePrimary}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, datePrimary: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Death date</span>
            <input
              type="date"
              value={form.dateSecondary}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, dateSecondary: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Relationship</span>
            <input
              value={form.relationship}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, relationship: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field full">
            <span>Memorial description</span>
            <textarea
              rows={4}
              value={form.description}
              onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
                setForm({ ...form, description: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field full">
            <span>Profile image URL (optional)</span>
            <input
              value={form.profileImage}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, profileImage: event.target.value })
              }
              placeholder="https://..."
            />
          </label>
        </>
      );
    }

    if (form.type === 'anniversary') {
      return (
        <>
          <label className="mm-detail-field">
            <span>Couple names</span>
            <input
              value={form.personName}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, personName: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Anniversary date</span>
            <input
              type="date"
              value={form.datePrimary}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, datePrimary: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Years together</span>
            <input
              value={form.yearsTogether}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, yearsTogether: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field full">
            <span>Special message</span>
            <textarea
              rows={4}
              value={form.specialMessage}
              onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
                setForm({ ...form, specialMessage: event.target.value })
              }
            />
          </label>
        </>
      );
    }

    if (form.type === 'wedding') {
      return (
        <>
          <label className="mm-detail-field">
            <span>Couple names</span>
            <input
              value={form.personName}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, personName: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Wedding date</span>
            <input
              type="date"
              value={form.datePrimary}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, datePrimary: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field">
            <span>Venue</span>
            <input
              value={form.venue}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setForm({ ...form, venue: event.target.value })
              }
            />
          </label>
          <label className="mm-detail-field full">
            <span>Special message</span>
            <textarea
              rows={4}
              value={form.specialMessage}
              onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
                setForm({ ...form, specialMessage: event.target.value })
              }
            />
          </label>
        </>
      );
    }

    return (
      <>
        <label className="mm-detail-field">
          <span>Person's name</span>
          <input
            value={form.personName}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setForm({ ...form, personName: event.target.value })
            }
          />
        </label>
        <label className="mm-detail-field">
          <span>Important date</span>
          <input
            type="date"
            value={form.datePrimary}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setForm({ ...form, datePrimary: event.target.value })
            }
          />
        </label>
        <label className="mm-detail-field">
          <span>Relationship</span>
          <input
            value={form.relationship}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setForm({ ...form, relationship: event.target.value })
            }
          />
        </label>
        <label className="mm-detail-field full">
          <span>Special message</span>
          <textarea
            rows={4}
            value={form.specialMessage}
            onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
              setForm({ ...form, specialMessage: event.target.value })
            }
          />
        </label>
      </>
    );
  };

  const renderWizardPanel = () => {
    if (wizardStep === 1) {
      return (
        <section className="mm-template-page">
          <div className="mm-template-card">
            <h2>Choose your occasion</h2>
            <p>What kind of memory are you celebrating?</p>

            <div className="mm-template-grid">
              {occasions
                .filter((occasion: OccasionCard) =>
                  ['birthday', 'wedding', 'graduation', 'anniversary', 'baby_shower', 'trip_memory', 'memorial', 'custom'].includes(occasion.key)
                )
                .map((occasion: OccasionCard) => (
                  <button
                    key={occasion.key}
                    className={form.type === occasion.key ? 'mm-template-option active' : 'mm-template-option'}
                    onClick={() => setForm({
                      ...form,
                      type: occasion.key,
                      title:
                        occasion.key === 'birthday'
                          ? "Mom's 60th Birthday Party"
                          : occasion.key === 'memorial'
                          ? 'In Memory of Grandpa'
                          : occasion.label
                    })}
                  >
                    <div className="mm-template-icon">{occasion.emoji}</div>
                    <strong>{occasion.label === 'Trip Memory' ? 'Vacation' : occasion.label === 'Custom' ? 'Other' : occasion.label}</strong>
                    <small>{occasion.subtitle}</small>
                  </button>
                ))}
            </div>

            <label className="mm-template-title-field">
              <span>Give your project a title</span>
              <input
                value={form.title}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setForm({ ...form, title: event.target.value })
                }
              />
            </label>

            <div className="mm-template-actions">
              <button className="mm-template-back-button" onClick={() => setScreen('dashboard')}>
                Back
              </button>
              <button className="mm-template-next-button" onClick={handleWizardNext}>
                Continue to Upload →
              </button>
            </div>
          </div>
        </section>
      );
    }

    if (wizardStep === 2) {
      return (
        <section className="mm-create-card mm-memorial-create">
          <div className="mm-memorial-header">
            <div>
              <span className="mm-kicker">Create a memorial</span>
              <h2>Create a beautiful tribute page</h2>
              <p>Combine profile details, life dates, tribute notes, and media into one memorial flow.</p>
            </div>
            <div className="mm-memorial-header-badge">Step 2 of 7</div>
          </div>

          <div className="mm-memorial-layout">
            <div className="mm-memorial-preview">
              <div className="mm-memorial-preview-card">
                <div className="mm-memorial-photo-ring">
                  <div className="mm-memorial-photo-core">{form.personName?.charAt(0) || 'M'}</div>
                </div>
                <h3>{form.personName || 'Loved One'}</h3>
                <p>{form.relationship || 'Forever remembered'}</p>
                <div className="mm-memorial-dates">
                  <span>{form.datePrimary || 'Birth date'}</span>
                  <span>—</span>
                  <span>{form.dateSecondary || 'Passed date'}</span>
                </div>
              </div>

              <div className="mm-memorial-preview-note">
                <strong>Tribute preview</strong>
                <p>{form.description || 'A graceful and memorable space to honor a life beautifully lived.'}</p>
              </div>
            </div>

            <div className="mm-memorial-form-panel">
              <div className="mm-details-grid">
                <label className="mm-detail-field full">
                  <span>Memorial title</span>
                  <input
                    value={form.title}
                    onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                      setForm({ ...form, title: event.target.value })
                    }
                    placeholder="In Memory of..."
                  />
                </label>
                {renderOccasionFields()}
                <label className="mm-detail-field full">
                  <span>Short tribute line</span>
                  <input
                    value={form.caption}
                    onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                      setForm({ ...form, caption: event.target.value })
                    }
                    placeholder="A loving tribute shared with family and friends"
                  />
                </label>
              </div>
            </div>
          </div>
        </section>
      );
    }

    if (wizardStep === 3) {
      return (
        <section className="mm-upload-card">
          <div className="mm-upload-header">
            <div>
              <h2>Upload your memories</h2>
              <p>Add photos & videos — drag to reorder them</p>
            </div>
            <span>Formats: JPG, PNG, MP4, MOV</span>
          </div>

          <div className="mm-dropzone">
            <div className="mm-upload-icon">⇪</div>
            <h3>Drop your photos & videos here</h3>
            <p>Your uploaded files appear below and will be used for auto-generated video creation</p>
            <div className="mm-format-row">Images: 5MB max • Videos: 50MB max</div>
            <label className="mm-primary-button small mm-upload-browse" htmlFor="mm-upload-files-input">
              {isSubmitting ? 'Uploading...' : 'Browse Files'}
            </label>
            <input
              id="mm-upload-files-input"
              type="file"
              accept="image/*,video/*"
              multiple
              className="mm-upload-input"
              onChange={(event: React.ChangeEvent<HTMLInputElement>) => uploadMediaFiles(event.target.files)}
            />
          </div>

          <div className="mm-upload-toolbar">
            <span>{currentMedia.length} items uploaded</span>
            <div className="mm-upload-toolbar-buttons">
              <button className="mm-toolbar-button" onClick={toggleSelectAllMedia}>
                {selectedMediaIds.length === currentMedia.length && currentMedia.length > 0 ? 'Clear Selection' : 'Select All'}
              </button>
              <button className="mm-toolbar-button" onClick={() => setIsReorderMode((current: boolean) => !current)}>
                {isReorderMode ? 'Done Reordering' : 'Reorder'}
              </button>
              <button className="mm-toolbar-button danger" onClick={removeSelectedMedia}>
                Remove
              </button>
            </div>
          </div>

          <div className="mm-media-grid">
            {currentMedia.map((item: MediaItem, index: number) => (
              <div
                className={selectedMediaIds.includes(item.id) ? `mm-media-tile tone-${item.tone} selected` : `mm-media-tile tone-${item.tone}`}
                key={item.id}
                onClick={() => toggleMediaSelection(item.id)}
              >
                {item.previewUrl ? (
                  item.kind === 'video' ? (
                    <video className="mm-memory-preview" src={item.previewUrl} muted />
                  ) : (
                    <img className="mm-memory-preview" src={item.previewUrl} alt={item.name || `memory-${index + 1}`} />
                  )
                ) : null}
                <span>{item.orderIndex}</span>
                <div className="mm-checkmark">{item.kind === 'video' ? '▶' : '✓'}</div>
                {isReorderMode && (
                  <div className="mm-memory-reorder">
                    <button type="button" onClick={(event: React.MouseEvent<HTMLButtonElement>) => {
                      event.stopPropagation();
                      moveMediaItem(index, index - 1);
                    }}>↑</button>
                    <button type="button" onClick={(event: React.MouseEvent<HTMLButtonElement>) => {
                      event.stopPropagation();
                      moveMediaItem(index, index + 1);
                    }}>↓</button>
                  </div>
                )}
              </div>
            ))}
          </div>

          <label className="mm-message-box">
            <span>Add a message or caption (optional)</span>
            <textarea
              rows={3}
              value={form.caption}
              onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
                setForm({ ...form, caption: event.target.value })
              }
            />
          </label>
        </section>
      );
    }

    if (wizardStep === 4) {
      return (
        <section className="mm-upload-card">
          <div className="mm-upload-header">
            <div>
              <h2>Generate your video</h2>
              <p>Backend generation moves the project from draft to processing to completed.</p>
            </div>
            <span>Status: draft → processing → completed</span>
          </div>

          <div className="mm-processing-card">
            <div className="mm-processing-orb" />
            <h3>{isSubmitting ? 'Generating video...' : 'Ready to generate'}</h3>
            <p>
              Your uploaded photos and videos will be combined to create an AI-style generated video, then a QR code will be prepared for instant access.
            </p>
            <div className="mm-processing-meta">
              <span>Template: {getCategory(form.type)}</span>
              <span>Output: MP4 • 1080p</span>
              <span>Items: {currentMedia.length}</span>
            </div>
          </div>
        </section>
      );
    }

    if (wizardStep === 5) {
      return (
        <section className="mm-upload-card">
          <div className="mm-upload-header">
            <div>
              <h2>Preview your generated video</h2>
              <p>Review the completed reel before deciding to recreate or publish.</p>
            </div>
            <span>Status: completed</span>
          </div>

          <div className="mm-video-card preview">
            <div className="mm-video-player">
              {currentProject?.videoUrl ? (
                <video className="mm-generated-video" src={currentProject.videoUrl} controls />
              ) : (
                <>
                  <div className="mm-play-circle">▶</div>
                  <div className="mm-video-timer">7:34</div>
                </>
              )}
            </div>
            <div className="mm-progress-row">
              <div className="mm-progress-bar">
                <div className="mm-progress-value" />
              </div>
              <span>0:48 / 7:34</span>
            </div>
          </div>

          <div className="mm-about-card no-margin">
            <h3>About this video</h3>
            <p>
              This generated video is created from your uploaded media sequence. Once published, scanning the QR code opens the shared video page instantly.
            </p>
            <div className="mm-inline-qr-preview">
              {currentProject?.qrUrl ? (
                <img src={currentProject.qrUrl} alt="Generated QR code" />
              ) : (
                <div className="mm-inline-qr-placeholder">QR</div>
              )}
              <div className="mm-inline-qr-content">
                <strong>{currentProject?.status === 'published' ? 'Scan to open the shared video page' : 'Publish to generate a scannable QR code'}</strong>
                <span>{getPublicVideoLink(currentProject) || 'Public video link will appear here after publish'}</span>
              </div>
            </div>
            <p>{form.caption || form.description || form.specialMessage}</p>
          </div>
        </section>
      );
    }

    if (wizardStep === 6) {
      return (
        <section className="mm-upload-card">
          <div className="mm-upload-header">
            <div>
              <h2>Recreate your reel</h2>
              <p>Optional step to regenerate if changes are needed.</p>
            </div>
            <span>Status remains completed after regeneration</span>
          </div>

          <div className="mm-processing-card">
            <div className="mm-processing-orb alt" />
            <h3>Need another version?</h3>
            <p>
              Regeneration replaces the previous generated video for this project while keeping the same draft content.
            </p>
            <div className="mm-processing-meta">
              <span>Current version: {generationCount}</span>
              <span>Previous output replaced on regenerate</span>
            </div>
          </div>
        </section>
      );
    }

    return (
      <section className="mm-upload-card mm-qr-page">
        <button className="mm-back-link mm-qr-back-link" onClick={() => setScreen('projects')}>
          ← Back to Project
        </button>

        <div className="mm-qr-page-heading">
          <h2>Your QR Code is ready</h2>
          <p>Share this code to let anyone view the video</p>
        </div>

        <div className="mm-qr-design-card">
          <div className="mm-qr-design-badge">● Scan to watch the video</div>

          <div className="mm-qr-design-frame">
            {currentProject?.qrUrl ? (
              <img className="mm-qr-image" src={currentProject.qrUrl} alt="Project QR code" />
            ) : (
              <div className="mm-qr-pattern">
                <span className="mm-qr-corner top-left" />
                <span className="mm-qr-corner top-right" />
                <span className="mm-qr-corner bottom-left" />
                <div className="mm-qr-dots">
                  {Array.from({ length: 64 }).map((_, index) => (
                    <span key={index} className={index % 7 === 0 || index % 11 === 0 ? 'accent' : ''} />
                  ))}
                </div>
              </div>
            )}
          </div>

          <h3>{currentProject?.title || "Mom's 60th Birthday Party"}</h3>
          <p className="mm-qr-design-link">{getPublicVideoLink(currentProject) || 'Publish to generate a public video link'}</p>

          <div className="mm-qr-design-actions">
            <button className="mm-qr-primary-button" onClick={() => currentProject?.qrUrl && window.open(currentProject.qrUrl, '_blank')}>
              Open QR PNG
            </button>
            <button
              className="mm-qr-secondary-button"
              onClick={() => {
                const link = getPublicVideoLink(currentProject);
                if (link) {
                  navigator.clipboard.writeText(link);
                }
              }}
            >
              Copy link
            </button>
          </div>
        </div>

        <div className="mm-qr-guide-card">
          <h4>How to use your QR code</h4>
          <div className="mm-qr-guide-list">
            <div className="mm-qr-guide-item">
              <span>1</span>
              <p>Print or display this QR code at your event</p>
            </div>
            <div className="mm-qr-guide-item">
              <span>2</span>
              <p>Guests scan it with any smartphone camera</p>
            </div>
            <div className="mm-qr-guide-item">
              <span>3</span>
              <p>They can instantly watch and share the video</p>
            </div>
          </div>
        </div>
      </section>
    );
  };

  const wizardActions = () => {
    if (wizardStep === 5) {
      return (
        <div className="mm-create-actions">
          <button className="mm-secondary-button" onClick={handleWizardBack}>
            ← Back
          </button>
          <div className="mm-action-group">
            <button className="mm-toolbar-button" onClick={() => setWizardStep(6)}>
              Recreate
            </button>
            <button className="mm-primary-button wide" onClick={publishProject}>
              Publish
            </button>
          </div>
        </div>
      );
    }

    if (wizardStep === 7) {
      return (
        <div className="mm-create-actions">
          <button className="mm-secondary-button" onClick={() => setScreen('dashboard')}>
            Dashboard
          </button>
          <button className="mm-primary-button wide" onClick={() => setScreen('publicView')}>
            View Public Page
          </button>
        </div>
      );
    }

    return (
      <div className="mm-create-actions">
        <button className="mm-secondary-button" onClick={handleWizardBack}>
          {wizardStep === 1 ? 'Back to Dashboard' : '← Back'}
        </button>
        <button className="mm-primary-button wide" onClick={handleWizardNext} disabled={isSubmitting}>
          {wizardStep === 4
            ? 'Generate Video'
            : wizardStep === 6
            ? 'Recreate Video'
            : wizardStep === 3
            ? 'Continue to Generate'
            : 'Continue'}
        </button>
      </div>
    );
  };

  if (screen === 'auth') {
    return (
      <div className="mm-auth-page">
        <div className="mm-auth-card">
          <div className="mm-logo-row">
            <div className="mm-logo-badge">✦</div>
            <span className="mm-logo-text">MemoReel</span>
          </div>

          <div className="mm-auth-tabs">
            <button
              className={authView === 'login' ? 'mm-auth-tab active' : 'mm-auth-tab'}
              onClick={() => setAuthView('login')}
            >
              Sign in
            </button>
            <button
              className={authView === 'register' ? 'mm-auth-tab active' : 'mm-auth-tab'}
              onClick={() => setAuthView('register')}
            >
              Create account
            </button>
          </div>

          <h1 className="mm-auth-title">{authView === 'login' ? 'Welcome back' : 'Create your account'}</h1>
          <p className="mm-auth-subtitle">
            {authView === 'login'
              ? 'Sign in to continue creating memories'
              : 'Start building beautiful memory reels'}
          </p>

          <div className="mm-form-stack">
            {authView === 'register' && (
              <label className="mm-field">
                <span>Full name</span>
                <input type="text" placeholder="Alex Johnson" />
              </label>
            )}
            <label className="mm-field">
              <span>Email address</span>
              <input type="email" placeholder="alex@example.com" />
            </label>
            <label className="mm-field">
              <span>Password</span>
              <input type="password" placeholder="••••••••" />
            </label>
          </div>

          <div className="mm-auth-meta">
            <span />
            <button className="mm-link-button" type="button">
              Forgot password?
            </button>
          </div>

          <button
            className="mm-primary-button"
            onClick={() =>
              saveAuth(
                authView === 'login' ? 'mock-jwt-token-1' : 'mock-jwt-token-2',
                'alex@example.com',
                'Alex Johnson'
              )
            }
          >
            {authView === 'login' ? 'Sign in' : 'Create account'}
          </button>

          <div className="mm-divider">or continue with</div>

          <button
            className="mm-social-button"
            onClick={() => {
              window.location.href = 'https://accounts.google.com/';
            }}
          >
            <span className="mm-google-icon">G</span>
            Continue with Google
          </button>

          <p className="mm-auth-footer">
            {authView === 'login' ? "Don't have an account? " : 'Already have an account? '}
            <button
              type="button"
              className="mm-inline-link"
              onClick={() => setAuthView(authView === 'login' ? 'register' : 'login')}
            >
              {authView === 'login' ? 'Sign up free' : 'Sign in'}
            </button>
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="mm-app-shell">
      <aside className="mm-sidebar">
        <div>
          <div className="mm-sidebar-brand">
            <div className="mm-logo-badge">✦</div>
            <span className="mm-logo-text">MemoReel</span>
          </div>

          <div className="mm-sidebar-group">
            <div className="mm-sidebar-label">Main</div>
            <button className={screen === 'dashboard' ? 'mm-sidebar-link active' : 'mm-sidebar-link'} onClick={() => setScreen('dashboard')}>
              Dashboard
            </button>
            <button className={screen === 'projects' ? 'mm-sidebar-link active' : 'mm-sidebar-link'} onClick={() => setScreen('projects')}>
              My Projects
            </button>
            <button className={screen === 'memorialCreate' ? 'mm-sidebar-link active' : 'mm-sidebar-link'} onClick={() => openCreateWizard('memorial')}>
              Memorials
            </button>
            <button className={screen === 'wizard' && wizardStep === 1 ? 'mm-sidebar-link active' : 'mm-sidebar-link'} onClick={() => {
              setWizardMode('create');
              setWizardStep(1);
              setScreen('wizard');
            }}>
              Templates
            </button>
          </div>

          <div className="mm-sidebar-group">
            <div className="mm-sidebar-label">Account</div>
            <button className={screen === 'settings' ? 'mm-sidebar-link active' : 'mm-sidebar-link'} onClick={() => setScreen('settings')}>
              Settings
            </button>
          </div>
        </div>

        <div className="mm-user-card">
          <div className="mm-avatar">AJ</div>
          <div>
            <div className="mm-user-name">{auth.user?.fullName ?? 'Alex Johnson'}</div>
            <div className="mm-user-plan">Pro Plan</div>
          </div>
        </div>
      </aside>

      <main className="mm-main-area">
        <header className="mm-topbar">
          <div className="mm-topbar-title">Good morning, Alex ✨</div>
          <div className="mm-topbar-actions">
            <button className="mm-icon-button">🔔</button>
            <button className="mm-profile-button" onClick={() => setScreen('settings')}>
              AJ
            </button>
          </div>
        </header>

        {screen === 'dashboard' && (
          <div className="mm-screen-content">
            <section className="mm-hero-card">
              <div>
                <h2>Your memory studio awaits</h2>
                <p>Create beautiful video stories from your photos and moments</p>
                <div className="mm-hero-actions">
                  <button className="mm-white-button" onClick={() => openCreateWizard()}>
                    + New Project
                  </button>
                  <button className="mm-glass-button" onClick={() => openCreateWizard('memorial')}>
                    Create Memorial
                  </button>
                </div>
              </div>
            </section>

            <section className="mm-stats-grid">
              <div className="mm-stat-box">
                <span>Total Projects</span>
                <strong>{stats.totalProjects}</strong>
                <small>All projects</small>
              </div>
              <div className="mm-stat-box">
                <span>Completed Projects</span>
                <strong>{stats.completedProjects}</strong>
                <small>Ready to publish</small>
              </div>
              <div className="mm-stat-box">
                <span>Published Projects</span>
                <strong>{stats.publishedProjects}</strong>
                <small>Live memorials</small>
              </div>
              <div className="mm-stat-box">
                <span>Privacy</span>
                <strong>Public</strong>
                <small>Phase 1 active</small>
              </div>
            </section>

            <section className="mm-projects-section">
              <div className="mm-section-header">
                <h3>Recent Projects</h3>
                <button className="mm-link-button" onClick={() => setScreen('projects')}>View all →</button>
              </div>

              <div className="mm-project-grid">
                {projects.map((project: Project, index: number) => (
                  <article className="mm-project-card" key={project.id}>
                    <div className={`mm-project-visual theme-${index % 3}`}>
                      <div className="mm-project-icon">{project.status === 'published' ? '▶' : project.status === 'completed' ? '✓' : '✦'}</div>
                    </div>
                    <div className="mm-project-body">
                      <h4>{project.title}</h4>
                      <div className="mm-project-badges">
                        <span className="mm-text-badge">{project.category}</span>
                        <span className={`mm-status-badge ${project.status}`}>{project.status}</span>
                      </div>
                      <div className="mm-project-meta">
                        Updated {new Date(project.updatedAt).toLocaleDateString()}
                      </div>
                      <div className="mm-project-actions">
                        <button className="mm-mini-link mm-delete-link" onClick={() => deleteProject(project.id)}>
                          Delete
                        </button>
                        {project.status === 'draft' && (
                          <>
                            <button className="mm-mini-link" onClick={() => openDraftProject(project)}>Edit</button>
                            <button className="mm-mini-link" onClick={() => openDraftProject(project)}>Generate</button>
                          </>
                        )}
                        {project.status === 'completed' && (
                          <>
                            <button className="mm-mini-link" onClick={() => openDraftProject(project)}>Preview</button>
                            <button className="mm-mini-link" onClick={() => {
                              setActiveProjectId(project.id);
                              setForm(project.metadata);
                              setWizardMode('edit');
                              setWizardStep(5);
                              setScreen('wizard');
                            }}>Publish</button>
                          </>
                        )}
                        {project.status === 'published' && (
                          <>
                            <button className="mm-mini-link" onClick={() => {
                              setActiveProjectId(project.id);
                              setScreen('publicView');
                            }}>View</button>
                            <button className="mm-mini-link" onClick={() => {
                              setActiveProjectId(project.id);
                              setForm(project.metadata);
                              setWizardMode('edit');
                              setWizardStep(7);
                              setScreen('wizard');
                            }}>QR Code</button>
                          </>
                        )}
                      </div>
                    </div>
                  </article>
                ))}
              </div>
            </section>
          </div>
        )}

        {screen === 'projects' && (
          <div className="mm-screen-content">
            <section className="mm-section-header">
              <div>
                <h3>My Projects</h3>
                <p className="mm-auth-subtitle">All your reels, memorials, drafts, and published pages in one place.</p>
              </div>
              <button className="mm-primary-button small" onClick={() => setScreen('dashboard')}>
                Back to Dashboard
              </button>
            </section>

            <section className="mm-projects-section">
              <div className="mm-project-grid">
                {projects.map((project: Project, index: number) => (
                  <article className="mm-project-card" key={project.id}>
                    <div className={`mm-project-visual theme-${index % 3}`}>
                      <div className="mm-project-icon">{project.status === 'published' ? '▶' : project.status === 'completed' ? '✓' : '✦'}</div>
                    </div>
                    <div className="mm-project-body">
                      <h4>{project.title}</h4>
                      <div className="mm-project-badges">
                        <span className="mm-text-badge">{project.category}</span>
                        <span className={`mm-status-badge ${project.status}`}>{project.status}</span>
                      </div>
                      <div className="mm-project-meta">
                        Updated {new Date(project.updatedAt).toLocaleDateString()}
                      </div>
                      <div className="mm-project-actions">
                        <button className="mm-mini-link mm-delete-link" onClick={() => deleteProject(project.id)}>
                          Delete
                        </button>
                        <button className="mm-mini-link" onClick={() => {
                          setActiveProjectId(project.id);
                          setForm(project.metadata);
                          if (project.type === 'memorial' && project.status === 'draft') {
                            setScreen('memorialCreate');
                            return;
                          }
                          if (project.status === 'published') {
                            setScreen('publicView');
                            return;
                          }
                          openDraftProject(project);
                        }}>
                          Open
                        </button>
                        <button className="mm-mini-link" onClick={() => {
                          setActiveProjectId(project.id);
                          setForm(project.metadata);
                          if (project.type === 'memorial') {
                            setScreen('memorialCreate');
                          } else {
                            openDraftProject(project);
                          }
                        }}>
                          Edit
                        </button>
                      </div>
                    </div>
                  </article>
                ))}
              </div>
            </section>
          </div>
        )}

        {screen === 'memorialCreate' && (
          <div className="mm-memorial-screen">
            <button className="mm-back-link mm-memorial-back" onClick={() => setScreen('dashboard')}>
              ← Back to Dashboard
            </button>

            <section className="mm-memorial-exact-page">
              <div className="mm-memorial-icon-circle">♡</div>
              <h1 className="mm-memorial-exact-title">Create a Memorial</h1>
              <p className="mm-memorial-exact-subtitle">
                A gentle space to honour and celebrate
                <br />
                the life of someone dear to you
              </p>

              <div className="mm-memorial-section-card">
                <div className="mm-memorial-section-label">About the person</div>

                <label className="mm-memorial-photo-upload" htmlFor="mm-memorial-photo-input">
                  {form.profileImage ? (
                    <img className="mm-memorial-photo-preview" src={form.profileImage} alt="Memorial profile" />
                  ) : (
                    <div className="mm-memorial-photo-upload-icon">◌</div>
                  )}
                  <strong>{form.profileImage ? 'Photo added' : 'Add a photo'}</strong>
                  <span>
                    {form.profileImage ? 'Click to change the uploaded photo' : 'Choose a photo that best represents them'}
                  </span>
                  <input
                    id="mm-memorial-photo-input"
                    type="file"
                    accept="image/*"
                    className="mm-memorial-photo-input"
                    onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                      const file = event.target.files?.[0];
                      if (!file) {
                        return;
                      }

                      const reader = new FileReader();
                      reader.onload = () => {
                        setForm({
                          ...form,
                          profileImage: typeof reader.result === 'string' ? reader.result : '',
                          type: 'memorial'
                        });
                      };
                      reader.readAsDataURL(file);
                    }}
                  />
                </label>

                <div className="mm-memorial-form-grid">
                  <label className="mm-memorial-field full">
                    <span>Full name</span>
                    <input
                      value={form.personName}
                      onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                        setForm({ ...form, personName: event.target.value, type: 'memorial' })
                      }
                      placeholder="Robert James Williams"
                    />
                  </label>

                  <label className="mm-memorial-field">
                    <span>Date of birth</span>
                    <input
                      value={form.datePrimary}
                      onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                        setForm({ ...form, datePrimary: event.target.value, type: 'memorial' })
                      }
                      placeholder="15-03-1942"
                    />
                  </label>

                  <label className="mm-memorial-field">
                    <span>Date of passing</span>
                    <input
                      value={form.dateSecondary}
                      onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                        setForm({ ...form, dateSecondary: event.target.value, type: 'memorial' })
                      }
                      placeholder="02-11-2024"
                    />
                  </label>
                </div>
              </div>

              <div className="mm-memorial-section-card">
                <div className="mm-memorial-section-label">Life story</div>

                <label className="mm-memorial-field full">
                  <span>A brief tribute</span>
                  <textarea
                    rows={4}
                    value={form.description}
                    onChange={(event: React.ChangeEvent<HTMLTextAreaElement>) =>
                      setForm({ ...form, description: event.target.value, type: 'memorial' })
                    }
                    placeholder="A loving father, devoted grandfather, and dear friend to many. Robert lived a life full of laughter, warmth, and quiet wisdom."
                  />
                </label>

                <label className="mm-memorial-field full">
                  <span>Relationship to you</span>
                  <select
                    value={form.relationship}
                    onChange={(event: React.ChangeEvent<HTMLSelectElement>) =>
                      setForm({ ...form, relationship: event.target.value, type: 'memorial' })
                    }
                  >
                    <option>Father / Father figure</option>
                    <option>Mother / Mother figure</option>
                    <option>Grandfather</option>
                    <option>Grandmother</option>
                    <option>Partner</option>
                    <option>Sibling</option>
                    <option>Friend</option>
                  </select>
                </label>
              </div>

              <div className="mm-memorial-section-card">
                <div className="mm-memorial-section-label">Privacy</div>

                <div className="mm-memorial-privacy-row">
                  <div>
                    <strong>Visible to family & friends</strong>
                    <span>Anyone with the link can view this memorial</span>
                  </div>
                  <button type="button" className="mm-memorial-toggle" aria-label="privacy toggle">
                    <span />
                  </button>
                </div>
              </div>

              <div className="mm-memorial-bottom-actions">
                <button className="mm-memorial-draft-button" onClick={() => {
                  setForm({ ...form, type: 'memorial', title: form.title || `In Memory of ${form.personName || 'Loved One'}` });
                  setWizardMode('create');
                  setWizardStep(2);
                  ensureDraftProject();
                  setScreen('projects');
                }}>
                  Save as draft
                </button>
                <button className="mm-memorial-submit-button" onClick={() => {
                  setForm({
                    ...form,
                    type: 'memorial',
                    title: form.title || `In Memory of ${form.personName || 'Loved One'}`
                  });
                  setWizardMode('create');
                  setWizardStep(3);
                  ensureDraftProject();
                  setScreen('wizard');
                }}>
                  Create Memorial →
                </button>
              </div>
            </section>
          </div>
        )}

        {screen === 'wizard' && (
          <div className="mm-screen-content narrow">
            <button className="mm-back-link" onClick={() => setScreen('dashboard')}>
              ← Back to Dashboard
            </button>

            <div className="mm-steps seven">
              {[
                'Occasion',
                'Details',
                'Upload Media',
                'Generate',
                'Preview',
                'Recreate',
                'Publish'
              ].map((label: string, index: number) => {
                const stepNumber = (index + 1) as WizardStep;
                const isComplete = stepNumber < wizardStep;
                const isActive = stepNumber === wizardStep;

                return (
                  <div
                    key={label}
                    className={isActive ? 'mm-step active' : isComplete ? 'mm-step complete' : 'mm-step'}
                  >
                    <span>{isComplete ? '✓' : stepNumber}</span>
                    <p>{label}</p>
                  </div>
                );
              })}
            </div>

            {renderWizardPanel()}
            {wizardActions()}
          </div>
        )}

        {screen === 'publicView' && (
          <div className="mm-output-screen">
            <div className="mm-output-hero">
              <div className="mm-output-brand">MemoReel</div>
              <h1>{currentProject?.title}</h1>
              <p>Shared by Alex Johnson • {currentProject?.publishedAt ? new Date(currentProject.publishedAt).toLocaleDateString() : 'April 2026'}</p>
              <div className="mm-output-tags">
                <span>{currentProject?.category ?? 'Celebration'}</span>
                <span>{currentProject?.media.length ?? 0} memories</span>
                <span>Public</span>
              </div>
            </div>

            <div className="mm-output-body">
              <div className="mm-video-card">
                <div className="mm-video-player">
                  {currentProject?.videoUrl ? (
                    <video className="mm-generated-video" src={currentProject.videoUrl} controls />
                  ) : (
                    <>
                      <div className="mm-play-circle">▶</div>
                      <div className="mm-video-timer">7:34</div>
                    </>
                  )}
                </div>
                <div className="mm-progress-row">
                  <div className="mm-progress-bar">
                    <div className="mm-progress-value" />
                  </div>
                  <span>{currentProject?.videoUrl ? 'Playable published video' : '0:48 / 7:34'}</span>
                </div>
              </div>

              <div className="mm-about-card">
                <h3>About this video</h3>
                <p>{currentProject?.metadata.caption || currentProject?.metadata.description}</p>
              </div>

              <div className="mm-output-actions">
                <button className="mm-secondary-button" onClick={() => setScreen('dashboard')}>
                  Back to Dashboard
                </button>
                <button className="mm-primary-button" onClick={() => setScreen('wizard')}>
                  Manage Publish
                </button>
              </div>
            </div>
          </div>
        )}

        {screen === 'settings' && (
          <div className="mm-screen-content narrow">
            <section className="mm-settings-card">
              <div className="mm-section-header">
                <h3>Account Settings</h3>
                <button className="mm-link-button" onClick={() => setScreen('dashboard')}>
                  ← Dashboard
                </button>
              </div>

              <div className="mm-settings-layout">
                <div className="mm-settings-avatar-block">
                  <div
                    className="mm-settings-avatar"
                    style={settingsPhoto ? { backgroundImage: `url(${settingsPhoto})`, backgroundSize: 'cover', backgroundPosition: 'center', color: 'transparent' } : undefined}
                  >
                    {auth.user?.fullName?.split(' ').map((part: string) => part[0]).join('').slice(0, 2) ?? 'AJ'}
                  </div>
                  <label className="mm-toolbar-button" htmlFor="mm-settings-photo-input">
                    Upload photo
                  </label>
                  <input
                    id="mm-settings-photo-input"
                    type="file"
                    accept="image/*"
                    className="mm-upload-input"
                    onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                      const file = event.target.files?.[0];
                      if (!file) {
                        return;
                      }

                      const reader = new FileReader();
                      reader.onload = () => {
                        setSettingsPhoto(typeof reader.result === 'string' ? reader.result : null);
                      };
                      reader.readAsDataURL(file);
                    }}
                  />
                </div>

                <div className="mm-settings-form">
                  <label className="mm-field">
                    <span>Full name</span>
                    <input value={auth.user?.fullName ?? ''} readOnly />
                  </label>
                  <label className="mm-field">
                    <span>Email</span>
                    <input value={auth.user?.email ?? ''} readOnly />
                  </label>
                  <label className="mm-field">
                    <span>Password</span>
                    <input type="password" value={passwordValue} readOnly />
                  </label>

                  <div className="mm-settings-actions">
                    <button
                      className="mm-secondary-button"
                      onClick={() => setPasswordValue((current: string) => (current === 'password' ? 'newpassword123' : 'password'))}
                    >
                      Change Password
                    </button>
                    <button className="mm-primary-button" onClick={signOut}>
                      Sign out
                    </button>
                  </div>
                </div>
              </div>
            </section>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;

// Made with Bob
