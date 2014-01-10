uniform mat4 uMvp;

attribute vec3 aPosition;
attribute vec2 aTexCoord; 
attribute vec4 aColor;

varying vec4 vColor;
varying vec2 vTexCoord;

void main() {
    // on calcule la position du point via la matrice de projection
    vec4 position = uMvp * vec4(aPosition.xyz, 1.);
    //vec4 position = vec4(aPosition.xyz, 1.);
    vColor = aColor;
    vTexCoord = aTexCoord;
    gl_PointSize = 10.;
	gl_Position =  position;
	
}
