// =====================================================================
// SuperCollider Workspace
// =====================================================================

(
SynthDef.new(\sine, {
	| freq=440, atk=0.01, rel=0.5, amp=0.8, pan=0, det=0 |
	var sig, env;
	sig = SinOsc.ar([freq-det, freq+det]);
	env = EnvGen.kr(Env.new([0,1,0], [atk, rel], [2,-2]), doneAction:2);
	sig = sig * [1-pan,1+pan];

	sig = sig * env * 0.1 * (1-(freq/1200));
	Out.ar(0, sig);
}).add;

p = Pbind(
	\instrument, \sine,
	\amp, 0.5,
	\dur,  Pexprand(0.15, 0.9, 50).round(0.15),
	\rel,  Pexprand(0.1, 2.0, inf),
	\freq, Pexprand(200, 2000, inf).round(50)
).play;
)

p.stop;

(
s.latency=0.05;
s.waitForBoot{

	var w    = Window("test", Rect(10,10,300,700), false);
	var u    = UserView(w, Rect(0,0,300,700));
	var pat  = Pn(Pexprand(80,1400,inf).round(80), inf).asStream;
	var rels = Pn(Pexprand(0.4,4,inf), inf).asStream;
	var atks = Pn(Pexprand(0.001,0.5,inf), inf).asStream;
	var pans = Pn(1.5*(Pwhite(0.1,0.9,inf)-0.5), inf).asStream;
	var amps = Pn(Pexprand(0.0,0.8,inf), inf).asStream;
	var dets = Pn(Pwhite(0.01,4,inf)-1, inf).asStream;

	
	var fre   = 100;
	var xlm   = 0.5;
	var rl    = 1;
	var at    = 1;
	var pn    = 0;
	var amp   = 0.8;
	var det   = 0.0;

	var nhis = 30;
	var xpos = Array.fill(nhis, 0);
	var ypos = Array.fill(nhis, 0);

	var xhist = Array.new();
	var yhist = Array.new();
	

	~slow=2;
	~radius=1.5;
	~lambda=3.3;
	
	u.drawFunc={
		if(u.frame%(1*~slow)==0, {
			xlm = ~lambda * xlm * (1.0 - xlm);
			fre = (xlm * 1400).round(80)/2;
			rl  = rels.next;
			at  = atks.next;
			pn  = pans.next;
			amp = amps.next;
			det = dets.next;
			x = Synth(\sine, [\freq, fre, \rel, rl, \atk, at, \pan, pn, \det, det ]);

			xpos.removeAt(0);
			ypos.removeAt(0);
			xpos.insert(xpos.size(), (200*~lambda)-550);
			ypos.insert(xpos.size(), 700-(700*xlm));
		});

		if( (u.frame%(200*~slow)==0) && ((~lambda+0.02) < 4), {
			~lambda=~lambda+0.02;
			~lambda.postln;
			xhist = xhist ++ xpos;
			yhist = yhist ++ ypos;
			xhist.size.postln;
		});

		if ( (xhist.size > 0),
			{
				xhist.do({
					arg item, i;
					Pen.fillColor   = Color.grey(0.4);
					Pen.strokeColor = Color.grey(0.4);
					Pen.fillOval(Rect.aboutPoint(Point(xhist.at(i),yhist.at(i)), ~radius, ~radius));
				});
			}
		);
			
		xpos.reverseDo({
			arg item, i;
			Pen.fillColor   = Color.grey((i/xpos.size())**2);
			Pen.strokeColor = Color.grey((i/xpos.size())**2);
			Pen.fillOval(Rect.aboutPoint(Point(xpos.at(i),ypos.at(i)), ~radius, ~radius));
		});

	};
	u.clearOnRefresh = true;
	u.canFocus = false;
	u.background = Color.black;
	w.front;
	u.animate=true;
	CmdPeriod.doOnce({if(w.isClosed.not, {w.close})});
};
)

~radius = 3.5;
~slow=1;
~lambda=3.0;

 s.plotTree;