"use strict";

const byId = (id) => document.getElementById(id);

document.addEventListener("DOMContentLoaded", async () => {
  setupNavigation();
  byId("currentYear").textContent = new Date().getFullYear().toString();

  try {
    const response = await fetch("profile.json", { cache: "no-cache" });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    const profile = await response.json();
    validateProfile(profile);
    renderProfile(profile);
    setupActions(profile);
  } catch (error) {
    console.error("Profile loading failed", error);
    showToast("No se ha podido cargar profile.json. Revisa que el archivo exista y sea JSON válido.");
  }
});

function validateProfile(profile) {
  if (!profile?.identity?.fullName || !profile?.contact?.websiteUrl) {
    throw new Error("Invalid profile schema");
  }
  if (!profile.contact.websiteUrl.startsWith("https://")) {
    throw new Error("websiteUrl must use HTTPS");
  }
}

function renderProfile(profile) {
  const { identity, contact } = profile;
  document.title = `${identity.fullName} · Digital Portfolio`;
  document.querySelector('meta[name="description"]').content = identity.shortBio;

  setText("brandInitials", identity.initials);
  setText("brandName", identity.fullName);
  setText("fullName", identity.fullName);
  setText("headline", identity.headline);
  setText("elevatorPitch", identity.elevatorPitch);
  setText("availability", identity.availability);
  setText("avatarInitials", identity.initials);
  setText("location", identity.location);
  setText("footerName", identity.fullName);
  setText("updatedAt", formatDate(profile.updatedAt));

  const photo = byId("profilePhoto");
  if (identity.photoUrl) {
    photo.src = identity.photoUrl;
    photo.alt = `Fotografía de ${identity.fullName}`;
    photo.hidden = false;
    byId("avatarInitials").hidden = true;
    photo.addEventListener("error", () => {
      photo.hidden = true;
      byId("avatarInitials").hidden = false;
    }, { once: true });
  }

  byId("templateBanner").hidden = !isTemplate(profile);
  renderHighlights(profile.highlights || []);
  renderServices(profile.services || []);
  renderOperations(profile.operations || []);
  renderExperience(profile.experience || []);
  renderProjects(profile.projects || []);
  renderSkills(profile.skills || []);
  configureLinks(contact);
  addStructuredData(profile);
}

function renderHighlights(items) {
  const container = byId("highlights");
  items.forEach((item) => {
    const card = element("div", "highlight");
    card.append(element("small", "", item.label), element("strong", "", item.value));
    container.append(card);
  });
}

function renderServices(services) {
  const symbols = { android: "A", mobile: "M", architecture: "{ }", operations: "↗" };
  const container = byId("services");
  services.forEach((service) => {
    const card = element("article", "info-card");
    card.append(
      element("div", "card-icon", symbols[service.icon] || "•"),
      element("h3", "", service.title),
      element("p", "", service.description),
    );
    container.append(card);
  });
}

function renderOperations(operations) {
  const container = byId("operations");
  operations.forEach((operation) => {
    const card = element("article", "operation");
    card.append(element("h3", "", operation.title), element("p", "", operation.description));
    container.append(card);
  });
}

function renderExperience(experience) {
  const container = byId("experience");
  experience.forEach((item) => {
    const article = element("article", "timeline-item");
    article.append(
      element("div", "timeline-period", item.period),
      element("h3", "", item.role),
      element("p", "timeline-company", item.company),
      element("p", "timeline-summary", item.summary),
    );
    if (item.achievements?.length) {
      const list = element("ul", "achievement-list");
      item.achievements.forEach((achievement) => list.append(element("li", "", achievement)));
      article.append(list);
    }
    container.append(article);
  });
}

function renderProjects(projects) {
  const container = byId("projects");
  projects.forEach((project) => {
    const card = element("article", "info-card project-card");
    card.append(
      element("div", "card-icon", "↗"),
      element("p", "card-meta", project.role),
      element("h3", "", project.name),
      element("p", "", project.summary),
    );
    const tags = element("div", "tag-list");
    (project.technologies || []).forEach((technology) => tags.append(element("span", "tag", technology)));
    card.append(tags);
    if (isWebUrl(project.url)) {
      const link = element("a", "card-link", "Ver proyecto →");
      link.href = project.url;
      link.target = "_blank";
      link.rel = "noopener noreferrer";
      card.append(link);
    }
    container.append(card);
  });
}

function renderSkills(skills) {
  const container = byId("skills");
  skills.forEach((skill) => container.append(element("span", "skill", skill)));
}

function configureLinks(contact) {
  const mailto = `mailto:${contact.email}`;
  setAnchor("emailButton", mailto);
  setAnchor("contactEmail", mailto);
  setAnchor("phoneLink", `tel:${contact.phone.replace(/\s/g, "")}`, contact.phone);
  setOptionalAnchor("linkedInLink", contact.linkedInUrl);
  setOptionalAnchor("githubLink", contact.githubUrl);
  setOptionalAnchor("cvButton", contact.cvUrl || "cv.html");
}

function setupActions(profile) {
  byId("shareButton").addEventListener("click", async () => {
    const shareData = {
      title: `${profile.identity.fullName} · Perfil profesional`,
      text: profile.identity.elevatorPitch,
      url: profile.contact.websiteUrl,
    };
    try {
      if (navigator.share) {
        await navigator.share(shareData);
      } else {
        await navigator.clipboard.writeText(profile.contact.websiteUrl);
        showToast("Enlace copiado");
      }
    } catch (error) {
      if (error?.name !== "AbortError") showToast("No se ha podido compartir el perfil");
    }
  });

  byId("vcardButton").addEventListener("click", () => downloadVCard(profile));
}

function downloadVCard(profile) {
  const { identity, contact } = profile;
  const lines = [
    "BEGIN:VCARD",
    "VERSION:3.0",
    `FN:${vcardEscape(identity.fullName)}`,
    `TITLE:${vcardEscape(identity.headline)}`,
    `EMAIL;TYPE=INTERNET:${contact.email}`,
    `TEL;TYPE=CELL:${contact.phone}`,
    `URL:${contact.websiteUrl}`,
    `NOTE:${vcardEscape(identity.elevatorPitch)}`,
    "END:VCARD",
  ];
  const blob = new Blob([lines.join("\r\n")], { type: "text/vcard;charset=utf-8" });
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = `${identity.fullName.toLowerCase().replace(/[^a-z0-9]+/gi, "-")}.vcf`;
  document.body.append(anchor);
  anchor.click();
  anchor.remove();
  URL.revokeObjectURL(url);
  showToast("Contacto preparado para guardar");
}

function addStructuredData(profile) {
  const data = {
    "@context": "https://schema.org",
    "@type": "Person",
    name: profile.identity.fullName,
    jobTitle: profile.identity.headline,
    description: profile.identity.shortBio,
    url: profile.contact.websiteUrl,
    email: profile.contact.email,
    telephone: profile.contact.phone,
    address: { "@type": "PostalAddress", addressLocality: profile.identity.location },
    sameAs: [profile.contact.linkedInUrl, profile.contact.githubUrl].filter(isWebUrl),
  };
  const script = document.createElement("script");
  script.type = "application/ld+json";
  script.textContent = JSON.stringify(data);
  document.head.append(script);
}

function setupNavigation() {
  const button = byId("menuButton");
  const menu = byId("mobileMenu");
  button.addEventListener("click", () => {
    const expanded = button.getAttribute("aria-expanded") === "true";
    button.setAttribute("aria-expanded", String(!expanded));
    menu.hidden = expanded;
  });
  menu.querySelectorAll("a").forEach((link) => link.addEventListener("click", () => {
    menu.hidden = true;
    button.setAttribute("aria-expanded", "false");
  }));
}

function element(tag, className = "", text = "") {
  const node = document.createElement(tag);
  if (className) node.className = className;
  if (text) node.textContent = text;
  return node;
}

function setText(id, value) {
  byId(id).textContent = value || "";
}

function setAnchor(id, href, text) {
  const anchor = byId(id);
  anchor.href = href;
  if (text) anchor.textContent = text;
}

function setOptionalAnchor(id, href) {
  const anchor = byId(id);
  if (href) {
    anchor.href = href;
    anchor.hidden = false;
    if (isWebUrl(href)) {
      anchor.target = "_blank";
      anchor.rel = "noopener noreferrer";
    }
  } else {
    anchor.hidden = true;
  }
}

function isWebUrl(value) {
  return typeof value === "string" && /^https:\/\//i.test(value);
}

function isTemplate(profile) {
  return profile.contact.websiteUrl.toUpperCase().includes("TU_USUARIO") ||
    profile.contact.email.toLowerCase().startsWith("tu-") ||
    profile.experience.some((item) => item.period.includes("[AAAA]"));
}

function formatDate(value) {
  const date = new Date(`${value}T00:00:00`);
  return Number.isNaN(date.getTime())
    ? value
    : new Intl.DateTimeFormat("es-ES", { dateStyle: "long" }).format(date);
}

function vcardEscape(value) {
  return String(value || "").replace(/\\/g, "\\\\").replace(/\n/g, "\\n").replace(/,/g, "\\,").replace(/;/g, "\\;");
}

let toastTimeout;
function showToast(message) {
  const toast = byId("toast");
  toast.textContent = message;
  toast.hidden = false;
  clearTimeout(toastTimeout);
  toastTimeout = setTimeout(() => { toast.hidden = true; }, 3200);
}
