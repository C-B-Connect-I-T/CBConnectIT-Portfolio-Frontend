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

**Parent Service / Sub-service**:
A hierarchical relation where a Sub-service references exactly one Parent Service, and a Parent Service can group multiple Sub-services.
_Avoid_: Category, child category, service group

**Top-level Service**:
A Service that has no Parent Service and is therefore eligible to act as a Parent Service in admin selection.
_Avoid_: Root node, primary category, standalone bucket

**Service Hierarchy Depth**:
The current portfolio service hierarchy is constrained to one level: Top-level Service → Sub-service.
_Avoid_: Arbitrary nesting, deep tree, multilevel taxonomy
