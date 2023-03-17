#define PROCESSING_COLOR_SHADER
#ifdef GL_ES
precision mediump float;
#endif

//VARIABLES ESPERADAS DESDE PROCESING
uniform float time; //VARIABLES QUE RECIBEN TODOS LOS THREADS DESDE LA CPU DE SOLO LECTURA.
uniform vec2 resolution;

//Utility functions

float field(in vec3 p,float s) {
	float strength = 7. + .03 * log(1.e-6 + fract(sin(time) * 12000.11));
	float accum = s/6.;
	float prev = 0.;
	float tw = 0.;
	for (int i = 0; i < 26; ++i) {
		float mag = dot(p, p);
		p = abs(p) / mag + vec3(-.5, -.4, -1.5);
		float w = exp(-float(i) / 7.);
		accum += w * exp(-strength * pow(abs(mag - prev), 2.2));
		tw += w;
		prev = mag;
	}
	return max(0., 5. * accum / tw - .7);
}

vec3 nrand3( vec2 co )
{
	vec3 a = fract( cos( co.x*10.3e-3 + co.y )*vec3(10.3e5, 4.7e5, 2.9e5) );
	vec3 b = fract( sin( co.x*0.8e-3 + co.y )*vec3(8.1e5, 1.0e5, 0.1e5) );
	vec3 c = mix(a, b, 0.5);
	return c;
}

/* ***********************************************************************
*
*                     MAIN
*
**************************************************************************/

void main() {
	vec4 asd;
	
    vec2 uv = 2. * gl_FragCoord.xy / resolution.xy - 1.;
	vec2 uvs = uv * resolution.xy / max(resolution.x, resolution.y);

	vec3 p = vec3(uvs / 4., 0) + vec3(1., -1.3, 0.);
	p += .2 * vec3(sin(time / 16.), sin(time / 12.),  sin(time / 128.));
	
	uvs.x -= sin(asd.x) - cos(asd.y) - tan(asd.z);
        uvs.y -= sin(asd.x) - cos(asd.y) - tan(asd.z);
	
	float freqs[4];
	freqs[0] = 0.04;
	freqs[1] = 0.5;
	freqs[2] = 0.12;
	freqs[3] = 0.4;
	
	float t = field(p,freqs[2]);
	float v = (1. - exp((abs(uv.x) - 1.) * 6.)) * (1. - exp((abs(uv.y) - 1.) * 6.));
	
	//Let's add some stars
	//Thanks to http://glsl.heroku.com/e#6904.0
	vec2 seed = p.xy * 1.1;	
	seed = floor(seed * resolution.x);
	vec3 rnd = nrand3( seed );
	vec4 starcolor = vec4(pow(rnd.y,19.0));
	
	gl_FragColor = mix(freqs[3]-.3, 1., v) * vec4(3.5*freqs[2] * t * t* t , 1.2*freqs[2] * t * t, freqs[3]*t, 1.0)+starcolor;
}