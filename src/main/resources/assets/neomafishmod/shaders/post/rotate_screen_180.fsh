#version 150

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 flippedUV = vec2(1.0 - texCoord.x, 1.0 - texCoord.y);
    vec4 color = texture(InSampler, flippedUV);
    fragColor = color;
}