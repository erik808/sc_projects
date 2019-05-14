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

	sig = sig * env * 0.1 * (1-(freq/1400));
	Out.ar(0, sig);
}).add;

// p = Pbind(
// 	\instrument, \sine,
// 	\amp, 0.5,
// 	\dur,  Pexprand(0.15, 0.9, 50).round(0.15),
// 	\rel,  Pexprand(0.1, 2.0, inf),
// 	\freq, Pexprand(200, 2000, inf).round(50)
// ).play;
)

p.stop;

(
s.latency=0.05;
s.waitForBoot{

	var w    = Window("test", Rect(10,10,300,700), false);
	var u    = UserView(w, Rect(0,0,300,700));
	var pat  = Pn(Pexprand(80,1400,inf).round(80), inf).asStream;
	var rels = Pn(Pexprand(1,10,inf), inf).asStream;
	var pans = Pn(Pexprand(0.2,0.9,inf)-0.55, inf).asStream;
	var amps = Pn(Pexprand(0.0,0.9,inf), inf).asStream;
	var dets = Pn(Pexprand(0.05,3,inf)-1, inf).asStream;

	
	var fre   = 0;
	var rl    = 1;
	var pn    = 0;
	var amp   = 0.8;
	var det   = 0.0;

	var nhis = 20;
	var xpos = Array.fill(nhis, 0);
	var ypos = Array.fill(nhis, 0);

	~fps=10;
	u.drawFunc={
		if(u.frame%(150/~fps)==0, {
			fre = pat.next;
			rl  = rels.next;
			pn  = pans.next;
			amp = amps.next;
			det = dets.next;
			x = Synth(\sine, [\freq, fre, \rel, rl, \pan, pn, \det, det ]);

			xpos.addFirst(150+(pn*150));
			ypos.addFirst(550-(fre/3));
			xpos.removeAt(xpos.size()-1);
			ypos.removeAt(ypos.size()-1);
		});
		

		xpos.do({
			arg item, i;
			Pen.fillColor   = Color.grey(1-(i/xpos.size()));
			Pen.strokeColor = Color.grey(1-(i/xpos.size()));
			Pen.fillOval(Rect.aboutPoint(Point(item,ypos.at(i)), 5, 5));
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

 s.plotTree;