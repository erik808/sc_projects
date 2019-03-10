// =====================================================================
// SuperCollider Workspace
// =====================================================================
(
a = {
	var baseTune = 362 / 8;
	var freqMult = [1, 4, 3, 10, 6, 9, 3, 2, 7, 11, 13, 15];
	var ampl   = 0.2;
	var phase  = 0;
	var output = 0;
	var envelope, sin, lfo;
	
	freqMult.do(
		{
			arg i, j;

			phase = 2*j*pi/freqMult.size;

			lfo      = SinOsc.kr((j+1)*0.01, 0, 0.01, 0.05);
			envelope = SinOsc.kr(lfo, phase, ampl/i, ampl/i);
			sin      = SinOsc.ar(i*baseTune, 0, envelope);
			output   = output + sin;
		}
	);
	
	output = output*SinOsc.kr(1/30, 0, 0.2, 0.8);
	Out.ar(0,output);
}.scope;
)

