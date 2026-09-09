"use strict";

const cvById = (id) => document.getElementById(id);

const skillGroups = [
  {
    title: "Desarrollo móvil y arquitectura",
    skills: [
      "Kotlin",
      "Java",
      "Android",
      "Android Studio",
      "Jetpack Compose",
      "Material Design",
      "iOS",
      "Clean Architecture",
      "SOLID",
      "Arquitectura modular",
      "MVVM / MVI / MVVI",
      "Koin",
      "Dagger Hilt",
    ],
  },
  {
    title: "Datos, servicios y plataforma",
    skills: [
      "Room",
      "SQLite",
      "Retrofit",
      "APIs REST",
      "Offline-first",
      "Firebase",
      "Analytics",
      "Google Maps y geolocalización",
      "Stripe · Adyen",
      "Twilio · Agora",
      "Google Play Store",
      "SQL · PL/SQL",
      "Microsoft Dynamics NAV · C/AL",
    ],
  },
  {
    title: "Calidad, entrega y colaboración",
    skills: [
      "Pruebas unitarias · JUnit 5 · Mockito",
      "Gradle",
      "Maven",
      "CI/CD · Bitrise · App Center · Jenkins",
      "Fastlane",
      "Git · GitHub · GitLab · Bitbucket · SVN",
      "Jira · Redmine · Trello · Slack",
      "Postman · Swagger · Charles Proxy",
      "Rendimiento",
      "Rendimiento Android · R8 · Android Vitals",
      "Accesibilidad",
    ],
  },
  {
    title: "Producto, diseño y operaciones",
    skills: [
      "Diseño de producto",
      "Dirección de operaciones",
      "Figma · Zeplin · Abstract",
      "HTML5 · CSS",
      "WordPress · Joomla",
    ],
  },
  {
    title: "Tecnologías complementarias",
    skills: [
      "C# · Unity 2D",
      "Blockchain Ethereum · Solidity · Remix",
      "VR · AR · Three.js",
    ],
  },
  {
    title: "Idiomas",
    skills: [
      "Español nativo",
      "Inglés intermedio",
    ],
  },
];

document.addEventListener("DOMContentLoaded", async () => {
  cvById("printButton").addEventListener("click", () => window.print());

  try {
    const response = await fetch("profile.json", { cache: "no-cache" });
    if (!response.ok) throw new Error("HTTP " + response.status);
    renderCv(await response.json());
  } catch (error) {
    console.error("CV loading failed", error);
    cvById("cvBio").textContent = "No se ha podido cargar la información del currículum.";
  }
});

function renderCv(profile) {
  const identity = profile.identity || {};
  const contact = profile.contact || {};

  document.title = "Currículum · " + (identity.fullName || "Espino Developer");
  setCvText("cvBrandInitials", identity.initials);
  setCvText("cvName", identity.fullName);
  setCvText("cvHeadline", identity.headline);
  setCvText("cvLocation", identity.location);
  setCvText("cvAvailability", identity.availability);
  setCvText("cvMonogram", identity.initials);
  setCvText("cvBio", identity.shortBio);
  setCvText("cvPitch", identity.elevatorPitch);
  renderUpdatedAt(profile.updatedAt);

  renderHighlights(profile.highlights);
  renderExperience(profile.experience);
  renderProjects(profile.projects);
  renderContact(contact);
  renderSkills(profile.skills);
  renderCredentials(profile.credentials);
}

function renderHighlights(highlights = []) {
  const container = cvById("cvHighlights");
  container.replaceChildren();

  highlights.forEach((highlight) => {
    const item = cvElement("article", "cv-highlight");
    item.append(
      cvElement("span", "cv-highlight-label", highlight.label),
      cvElement("strong", "", highlight.value),
    );
    container.append(item);
  });
}

function renderExperience(experience = []) {
  const container = cvById("cvExperience");
  container.replaceChildren();

  experience.forEach((item) => {
    const entry = cvElement("article", "cv-entry");
    const heading = cvElement("div", "cv-entry-heading");
    heading.append(
      cvElement("h3", "", item.role),
      cvElement("p", "meta", [item.company, item.period].filter(Boolean).join(" · ")),
    );
    entry.append(
      heading,
      cvElement("p", "cv-entry-summary", item.summary),
    );

    const achievements = Array.isArray(item.achievements) ? item.achievements.filter(Boolean) : [];
    if (achievements.length) {
      const list = cvElement("ul", "cv-achievement-list");
      achievements.forEach((achievement) => list.append(cvElement("li", "", achievement)));
      entry.append(list);
    }

    container.append(entry);
  });
}

function renderProjects(projects = []) {
  const container = cvById("cvProjects");
  container.replaceChildren();

  projects.forEach((project) => {
    const entry = cvElement("article", "cv-project");
    entry.append(
      cvElement("h3", "", project.name),
      cvElement("p", "meta", project.role),
      cvElement("p", "cv-project-summary", project.summary),
    );

    const technologies = Array.isArray(project.technologies) ? project.technologies.filter(Boolean) : [];
    if (technologies.length) {
      const tags = cvElement("div", "cv-project-tags");
      technologies.forEach((technology) => tags.append(cvElement("span", "cv-project-tag", technology)));
      entry.append(tags);
    }

    if (project.url) {
      const link = cvElement("a", "cv-project-link", "Ver proyecto");
      link.href = project.url;
      link.target = "_blank";
      link.rel = "noopener noreferrer";
      entry.append(link);
    }

    container.append(entry);
  });
}

function renderContact(contact) {
  const container = cvById("cvContact");
  container.replaceChildren();

  const details = [
    { label: "Email", text: contact.email, href: contact.email ? "mailto:" + contact.email : "" },
    { label: "Teléfono", text: contact.phone, href: contact.phone ? "tel:" + contact.phone.replace(/\s/g, "") : "" },
    { label: "Web", text: "Portfolio profesional", href: contact.websiteUrl },
    { label: "GitHub", text: "Espino", href: contact.githubUrl },
    { label: "WhatsApp", text: "Escribir por WhatsApp", href: contact.whatsAppUrl },
    { label: "LinkedIn", text: "Perfil profesional", href: contact.linkedInUrl },
  ];

  details.filter(({ href }) => Boolean(href)).forEach(({ label, text, href }) => {
    const item = cvElement("div", "cv-contact-item");
    const link = cvElement("a", "", text);
    link.href = href;
    link.setAttribute("aria-label", label + ": " + text);
    if (/^https?:\/\//.test(href)) {
      link.target = "_blank";
      link.rel = "noopener noreferrer";
    }

    item.append(cvElement("span", "", label), link);
    container.append(item);
  });
}

function renderSkills(skills = []) {
  const container = cvById("cvSkills");
  container.replaceChildren();

  const availableSkills = Array.isArray(skills) ? skills.filter(Boolean) : [];
  const knownSkills = new Set(skillGroups.flatMap((group) => group.skills));
  const groups = skillGroups
    .map((group) => ({
      title: group.title,
      skills: group.skills.filter((skill) => availableSkills.includes(skill)),
    }))
    .filter((group) => group.skills.length);

  const remainingSkills = availableSkills.filter((skill) => !knownSkills.has(skill));
  if (remainingSkills.length) {
    groups.push({ title: "Otros conocimientos", skills: remainingSkills });
  }

  groups.forEach((group) => {
    const section = cvElement("section", "cv-skill-group");
    const list = cvElement("div", "skill-list");
    group.skills.forEach((skill) => list.append(cvElement("span", "skill", skill)));
    section.append(cvElement("h3", "", group.title), list);
    container.append(section);
  });
}

function renderCredentials(credentials = []) {
  const container = cvById("cvCredentials");
  container.replaceChildren();

  credentials.forEach((credential) => {
    const item = cvElement("article", "cv-credential");
    item.append(
      cvElement("h3", "", credential.title),
      cvElement("p", "meta", [credential.issuer, credential.period].filter(Boolean).join(" · ")),
    );
    container.append(item);
  });
}

function renderUpdatedAt(value) {
  const time = cvById("cvUpdatedAt");
  if (!value) {
    time.textContent = "";
    time.removeAttribute("datetime");
    return;
  }

  const date = new Date(value + "T12:00:00");
  time.dateTime = value;
  time.textContent = Number.isNaN(date.getTime())
    ? value
    : new Intl.DateTimeFormat("es-ES", {
        day: "numeric",
        month: "long",
        year: "numeric",
      }).format(date);
}

function cvElement(tag, className = "", text = "") {
  const node = document.createElement(tag);
  if (className) node.className = className;
  if (text) node.textContent = text;
  return node;
}

function setCvText(id, value) {
  cvById(id).textContent = value || "";
}
