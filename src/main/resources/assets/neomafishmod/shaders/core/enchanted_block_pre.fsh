#version 150

#moj_import <minecraft:fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a < 0.1) {
        discard;
    }
    float value = log(vertexDistance + 0.5) / log(12 * 16 + 1.0);
    vec4 customColor = vec4(value, value, value, 1.0);
    fragColor = linear_fog(customColor, vertexDistance, FogStart, FogEnd, vec4(0.0,0.0,0.0,1.0));
}
