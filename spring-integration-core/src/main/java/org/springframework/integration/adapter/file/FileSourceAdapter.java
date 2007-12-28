/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.adapter.file;

import java.io.File;

import org.springframework.integration.adapter.PollingSourceAdapter;
import org.springframework.integration.channel.MessageChannel;
import org.springframework.integration.message.MessageMapper;
import org.springframework.util.Assert;

/**
 * Channel adapter for polling a directory and creating messages from its files.
 * 
 * @author Mark Fisher
 */
public class FileSourceAdapter extends PollingSourceAdapter<File> {

	public FileSourceAdapter(File directory, MessageChannel channel, int period) {
		this(directory, channel, period, true);
	}

	public FileSourceAdapter(File directory, MessageChannel channel, int period, boolean isTextBased) {
		super(new FileSource(directory));
		this.setChannel(channel);
		this.setPeriod(period);
		if (isTextBased) {
			this.setMessageMapper(new TextFileMapper(directory));
		}
		else {
			this.setMessageMapper(new ByteArrayFileMapper(directory));
		}
	}

	public void setBackupDirectory(File backupDirectory) {
		Assert.notNull(backupDirectory, "'backupDirectory' must not be null");
		MessageMapper<?, File> mapper = this.getMessageMapper();
		if (mapper != null && (mapper instanceof AbstractFileMapper<?>)) {
			((AbstractFileMapper<?>) mapper).setBackupDirectory(backupDirectory);
		}
	}

}
