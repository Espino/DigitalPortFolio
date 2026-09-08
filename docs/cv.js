"use strict";

const cvById = (id) => document.getElementById(id);

document.addEventListener("DOMContentLoaded", async () => {
  cvById("printButton").addEventListener("click", () => window.print());
  try {
    const response = await fetch("profile.json", { cache: "no-cache" });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    renderCv(await response.json());
  } catch (error) {
    console.error("CV loading failed", error);
    cvById("cvBio").textContent = "No se ha podido cargar profile.json.";
  }
});

function renderCv(profile) {
  const { identity, contact } = profile;
  document.title = `Currículum · ${identity.fullName}`;
  setCvText("cvBrandInitials", identity.initials);
  setCvText("cvName", identity.fullName);
  setCvText("cvHeadline", identity.headline);
  setCvText("cvLocation", identity.location);
  setCvText("cvMonogram", identity.initials);
  setCvText("cvBio", identity.shortBio);
  setCvText("cvPitch", identity.elevatorPitch);

  const experienceContainer = cvById("cvExperience");
  (profile.experience || []).forEach((item) => {
    const entry = cvElement("article", "cv-entry");
    entry.append(
      cvElement("h3", "", item.role),
      cvElement("p", "meta", `${item.company} · ${item.period}`),
      cvElement("p", "", item.summary),
    );
    const list = cvElement("ul");
    (item.achievements || []).forEach((achievement) => list.append(cvElement("li", "", achievement)));
    entry.append(list);
    experienceContainer.append(entry);
  });

  const projectContainer = cvById("cvProjects");
  (profile.projects || []).forEach((project) => {
    const entry = cvElement("article", "cv-entry");
    entry.append(
      cvElement("h3", "", project.name),
      cvElement("p", "meta", project.role),
      cvElement("p", "", project.summary),
    );
    projectContainer.append(entry);
  });

  const contactContainer = cvById("cvContact");
  addCvLink(contactContainer, contact.email, `mailto:${contact.email}`);
  addCvLink(contactContainer, contact.phone, `tel:${contact.phone.replace(/\s/g, "")}`);
  addCvLink(contactContainer, contact.websiteUrl, contact.websiteUrl);
  addCvLink(contactContainer, "LinkedIn", contact.linkedInUrl);
  addCvLink(contactContainer, "GitHub", contact.githubUrl);

  const skillsContainer = cvById("cvSkills");
  (profile.skills || []).forEach((skill) => skillsContainer.append(cvElement("span", "skill", skill)));

  const credentialsContainer = cvById("cvCredentials");
  (profile.credentials || []).forEach((credential) => {
    const item = cvElement("div", "cv-entry");
    item.append(
      cvElement("h3", "", credential.title),
      cvElement("p", "meta", `${credential.issuer} · ${credential.period}`),
    );
    credentialsContainer.append(item);
  });
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

function addCvLink(container, label, href) {
  if (!href) return;
  const anchor = cvElement("a", "", label);
  anchor.href = href;
  container.append(anchor);
}
