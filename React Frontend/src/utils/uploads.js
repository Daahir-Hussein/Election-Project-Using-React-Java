export const ACCEPTED_IMAGE_EXTENSIONS = '.jpg,.jpeg,.png';

const ACCEPTED_IMAGE_TYPES = new Set(['image/jpeg', 'image/png']);
const MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

export const UPLOAD_BASE_URL = (
  import.meta.env.VITE_UPLOAD_BASE_URL ||
  'http://localhost:8080/uploads'
).replace(/\/$/, '');

export function getCandidatePhotoUrl(photo) {
  if (!photo) return null;
  return `${UPLOAD_BASE_URL}/candidates/${encodeURIComponent(photo)}`;
}

export function getPartyLogoUrl(logo) {
  if (!logo) return null;
  return `${UPLOAD_BASE_URL}/parties/${encodeURIComponent(logo)}`;
}

export function validateImageFile(file) {
  if (!file) {
    return 'Please select an image file.';
  }

  if (!ACCEPTED_IMAGE_TYPES.has(file.type)) {
    return 'Only .jpg, .jpeg, and .png files are allowed.';
  }

  if (file.size > MAX_IMAGE_SIZE_BYTES) {
    return 'The image must not exceed 5 MB.';
  }

  return null;
}
