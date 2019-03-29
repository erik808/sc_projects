// =====================================================================
// SuperCollider Workspace
// =====================================================================
(
SynthDef.new(\overtone_osc, {
	arg tune = 362,	lfoFreq = 0.05;
	
	var freqMult = [1, 4, 3, 10, 6, 9, 3, 2, 7, 11, 13, 15];
	var baseTune = tune / 8;
	var ampl   = 0.2;
	var phase  = 0;
	var sig1   = 0;	
	var sig2   = 0;
	var envelope, sin1, sin2, lfo;
	
	freqMult.do(
		{
			arg i, j;
			
			phase = 2*j*pi/freqMult.size;

			lfo      = SinOsc.kr((j+1)*0.01, 0, 0.01, lfoFreq);
			envelope = SinOsc.kr(lfo, phase, ampl/i, ampl/i);
			sin1     = SinOsc.ar(i*baseTune, 0, envelope);
			sin2     = SinOsc.ar((i+1)*baseTune, 0.0, envelope);
			sig1     = sig1 + sin1;
			sig2     = sig2 + sin2;
		}
	);
	
	sig1 = sig1*SinOsc.kr(1/30, 0, 0.2, 0.8);
	sig2 = sig2*SinOsc.kr(1/30, 0, 0.2, 0.8);

	Out.ar(0,sig1);
	Out.ar(1,sig2);
}).add;
)