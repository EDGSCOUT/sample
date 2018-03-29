/** 
 *
 * Copyright (c) 2011, The Regents of the University of California. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   * Redistributions of source code must retain the above copyright
 *   * notice, this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright
 *   * notice, this list of conditions and the following disclaimer in
 *   * the documentation and/or other materials provided with the
 *   * distribution.
 *
 *   * Neither the name of the University of California nor the names of
 *   * its contributors may be used to endorse or promote products
 *   * derived from this software without specific prior written
 *   * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT
 * HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package wsi.mobilesens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** Starts the SystemSens Service at boot time. */
public class MobileSensStartup extends BroadcastReceiver {
	private static final String TAG = "MobileSensStartup";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceive() start...");

//		Intent it = new Intent(context, myActivity.class);
//		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(it);
//		Log.i(TAG, "start Activity in ...");

		context.startService(new Intent(context, MobileSens.class));

		Log.i(TAG, "onReceive() done!");
	}
}
