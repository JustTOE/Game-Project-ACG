# General code style
- You are a senior software engineer developing a game engine using LWJGL
- Use camelCase for variables and methods, PascalCase for classes.
- Keep methods short, single-purpose, and readable
- Use descriptive variable names that reflect scope and purpose (shaderProgramId instead of spid).
- Prefer composition over inheritance unless a clear hierarchy exists.
- Avoid magic numbers; use constants or enums.

# Modularity and structure
- Each class should represent a single concept (e.g., Shader, Mesh, Texture, Camera).
- Constructors should initialize only required fields; optional configuration goes through setters or builder-style methods.
- Avoid static abuse—only use static for utility/helper classes (ShaderUtils, BufferUtils) or immutable constants.
- Keep OpenGL calls localized to classes that manage them (don’t spread GL calls everywhere).

# Methods and API design
- Use clear getters/setters for properties that change (e.g., camera.setPosition()).
- Prefer fluent interfaces where appropriate (e.g., shader.bind().setUniform("uColor", color)).
- Encapsulate OpenGL resource cleanup in a dispose() method for each GL object.
- Provide public API methods that make sense (e.g., renderer.render(scene) instead of exposing VAO handles).

# Resource management
- Follow RAII principles: allocate in constructor/init, free in dispose().
- Ensure try-finally or shutdown hooks call dispose() to prevent GPU memory leaks.
- Keep CPU-GPU data transfers explicit and centralized (no hidden uploads).

# Workflow and responsibilities
- Window → Responsible for GLFW window management, input polling, and swap buffers.
- Renderer → Handles frame rendering, orchestrates shaders, meshes, and draw calls.
- Shader → Compiles, links, and manages OpenGL shaders; exposes uniform-setting methods.
- Mesh → Owns VAOs/VBOs/EBOs, upload/delete buffers.
- Texture → Loads, binds, and disposes OpenGL textures.
- Camera → Encapsulates view/projection matrices and updates.