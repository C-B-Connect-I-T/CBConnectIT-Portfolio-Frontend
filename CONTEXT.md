# Portfolio Domain

This context defines the portfolio entities managed through the site and backoffice.

## Language

**Job Position**:
The canonical role title attached to a person-facing entity (for example in experiences or testimonials).
_Avoid_: Function, role name, title

**Experience**:
A timeline entry that combines period, company, job position, narrative text, and related tags.
_Avoid_: Work note, role text, timeline row

**Testimonial**:
A personal recommendation entry that includes full name, company/job position context, profile image URL, and review text.
_Avoid_: Quote, review card, endorsement block

**Service**:
A portfolio offering that combines core copy, a required image, an optional banner image, and optional hierarchy/tag metadata.
_Avoid_: Capability, package, offering card

**Project**:
A portfolio case study that combines core copy, a required image, a required banner image, and optional tag/link associations.
_Avoid_: Case, work item, portfolio entry

**Project Link**:
A URL association attached to a Project, represented as a plain URL entry in admin input.
_Avoid_: Social handle, CTA button, rich link object

**Project Tag Set**:
The set of tags associated with a Project; it is non-empty in admin-managed data.
_Avoid_: Optional categories, untagged project state

**Project Media Pair**:
The required pair of media assets for a Project: one image and one banner image. Updates may replace either or both, but cannot leave the Project missing one.
_Avoid_: Removable banner, single-media project, optional second image

**Parent Service / Sub-service**:
A hierarchical relation where a Sub-service references exactly one Parent Service, and a Parent Service can group multiple Sub-services.
_Avoid_: Category, child category, service group

**Top-level Service**:
A Service that has no Parent Service and is therefore eligible to act as a Parent Service in admin selection.
_Avoid_: Root node, primary category, standalone bucket

**Service Hierarchy Depth**:
The current portfolio service hierarchy is constrained to one level: Top-level Service → Sub-service.
_Avoid_: Arbitrary nesting, deep tree, multilevel taxonomy
