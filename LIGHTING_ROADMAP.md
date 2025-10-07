# Lighting System Implementation Roadmap

## Current Status (Basic Color-Based Lighting)

### ✅ Completed
1. **Light data class** (`Light.java`)
   - Position, color, intensity properties
   - Getters/setters for dynamic light manipulation

2. **Basic lighting shaders** (`lighting.vert.glsl`, `lighting.frag.glsl`)
   - Simple color multiplication: `objectColor * lightColor`
   - No normal vectors yet (placeholder for future Phong lighting)

3. **Light source visualization shaders** (`lightCube.vert.glsl`, `lightCube.frag.glsl`)
   - Renders light position as a bright white cube
   - Unaffected by lighting calculations

4. **LightingManager** (`LightingManager.java`)
   - Manages VAOs for lit objects and light cubes
   - `renderLitObject()` - renders objects affected by light
   - `renderLightCube()` - renders light source visualization
   - Proper resource cleanup with `dispose()`

5. **Shader API enhancements** (`Shader.java`)
   - Added `setVector3f()` methods for vec3 uniforms

### Current Architecture
```
Shader Systems:
├── vertex.glsl + fragment.glsl          → Textured objects (10 cubes, currently disabled)
├── lighting.vert.glsl + lighting.frag.glsl  → Lit objects (simple color-based)
└── lightCube.vert.glsl + lightCube.frag.glsl  → Light source visualization
```

---

## Phase 1: Add Normal Vectors to Vertex Format
**Goal:** Prepare vertex data for proper Phong/Blinn-Phong lighting

### Tasks
1. **Update vertex data structure**
   - Current format: `Position(3) + Color(3) + TexCoords(2)` = 8 floats/vertex
   - New format: `Position(3) + Normal(3) + Color(3) + TexCoords(2)` = 11 floats/vertex
   - Update cube vertices array in `Application` class to include normals

2. **Update ModelLoader**
   - Modify `loadModel()` to handle new vertex stride (11 floats instead of 8)
   - Update `glVertexAttribPointer` calls:
     - Location 0: Position (3 floats, offset 0)
     - Location 1: Normal (3 floats, offset 3)
     - Location 2: Color (3 floats, offset 6)
     - Location 3: TexCoords (2 floats, offset 9)

3. **Update lighting shaders**
   - Add normal input to vertex shader: `layout (location = 1) in vec3 aNormal;`
   - Pass normals to fragment shader via `out vec3 Normal;`
   - Calculate normal matrix: `mat3(transpose(inverse(model)))`

4. **Update LightingManager**
   - Modify vertex array setup to include normal attribute pointer
   - Update VERTICES array to include normals for each face

---

## Phase 2: Implement Phong Lighting Model
**Goal:** Add realistic ambient, diffuse, and specular lighting

### Tasks
1. **Create Material class** (`Material.java`)
   ```java
   class Material {
       Vector3f ambient;   // How much ambient light the material reflects
       Vector3f diffuse;   // Main color under direct light
       Vector3f specular;  // Shininess/highlight color
       float shininess;    // Concentration of specular highlight (1-256)
   }
   ```

2. **Update Light class**
   - Add attenuation properties:
     - `float constant` (usually 1.0)
     - `float linear` (for distance falloff)
     - `float quadratic` (for distance falloff)

3. **Update lighting fragment shader** (`lighting.frag.glsl`)
   - Add uniforms:
     ```glsl
     uniform vec3 lightPos;
     uniform vec3 viewPos;
     uniform Material material;
     uniform Light light;
     ```
   - Implement Phong lighting calculations:
     - **Ambient**: `ambient = light.ambient * material.ambient`
     - **Diffuse**: `diffuse = light.diffuse * (max(dot(norm, lightDir), 0.0) * material.diffuse)`
     - **Specular**: `specular = light.specular * (pow(max(dot(viewDir, reflectDir), 0.0), material.shininess) * material.specular)`
     - **Attenuation**: `attenuation = 1.0 / (constant + linear * distance + quadratic * distance²)`
   - Final color: `(ambient + diffuse + specular) * attenuation`

4. **Update LightingManager**
   - Modify `renderLitObject()` to accept `Material` parameter
   - Pass camera position for specular calculations
   - Set all required uniforms (light position, view position, material properties)

---

## Phase 3: Convert Existing Textured Cubes to Lit System
**Goal:** Apply lighting to the existing 10 textured cubes

### Tasks
1. **Create new lit + textured shaders**
   - `litTextured.vert.glsl` - combines lighting calculations with texture sampling
   - `litTextured.frag.glsl` - applies Phong lighting to textured surfaces
   - Texture color becomes the diffuse material property

2. **Update Main.java render loop**
   - Re-enable the 10 cube rendering
   - Use new lit textured shaders instead of basic shaders
   - Apply same lighting calculations as solid-color lit objects

3. **Optional: Add multiple light sources**
   - Extend `LightingManager` to handle multiple lights
   - Update fragment shader to accumulate lighting from all sources
   - Render multiple light cubes for visualization

---

## Phase 4: Advanced Lighting Features (Future)
**Optional enhancements for later:**

- **Blinn-Phong model** (more efficient specular calculation)
- **Directional lights** (sun-like infinite distance lights)
- **Spotlights** (cone-shaped lights with cutoff angles)
- **Normal mapping** (fake surface detail without geometry)
- **Shadow mapping** (real-time shadows)
- **Deferred rendering** (for many lights efficiently)

---

## Architecture Notes

### Shader Separation Strategy
- **Basic shaders** (`vertex.glsl`, `fragment.glsl`): Simple textured rendering, no lighting
- **Lighting shaders** (`lighting.vert.glsl`, `lighting.frag.glsl`): Color-based lit objects
- **Light cube shaders** (`lightCube.vert.glsl`, `lightCube.frag.glsl`): Light visualization only
- **Future lit-textured shaders**: Will combine textures with Phong lighting

### Why Not Use One Shader for Everything?
- **Performance**: Different shaders optimized for different use cases
- **Flexibility**: Easy to toggle lighting on/off per object
- **Debugging**: Can isolate lighting issues from texture issues
- **Light sources**: Need to render bright regardless of scene lighting

### Resource Management
- All shaders follow RAII: initialize in setup, dispose in cleanup
- LightingManager owns its VAOs/VBOs, cleans up in `dispose()`
- Follow existing pattern with ResourceManager for centralized cleanup

---

## References
- C++ template in `CLAUDE.md` (lines 32-128)
- Current shader implementations in `src/main/resources/shaders/`
- Existing code style guide in `CLAUDE.md` (lines 1-25)