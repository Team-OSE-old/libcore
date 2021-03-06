/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package java.lang;

import com.android.dex.Dex;
import java.lang.reflect.ArtField;
import java.lang.reflect.ArtMethod;

/**
 * A dex cache holds resolved copies of strings, fields, methods, and classes from the dexfile.
 */
final class DexCache {
    /** Lazily initialized dex file wrapper. Volatile to avoid double-check locking issues. */
    private volatile Dex dex;

    /** Indexed by the type index array of locations of initialized static storage. */
    Object[] initializedStaticStorage;

    /** The location of the associated dex file. */
    String location;

    /**
     * References to fields as they become resolved following interpreter semantics. May refer to
     * fields defined in other dex files.
     */
    ArtField[] resolvedFields;

    /**
     * References to methods as they become resolved following interpreter semantics. May refer to
     * methods defined in other dex files.
     */
    ArtMethod[] resolvedMethods;

    /**
     * References to types as they become resolved following interpreter semantics. May refer to
     * types defined in other dex files.
     */
    Class[] resolvedTypes;

    /**
     * References to strings as they become resolved following interpreter semantics. All strings
     * are interned.
     */
    String[] strings;

    /** Holds C pointer to dexFile. */
    private int dexFile;

    // Only created by the VM.
    private DexCache() {}

    Dex getDex() {
        Dex result = dex;
        if (result == null) {
            synchronized (this) {
                result = dex;
                if (result == null) {
                    dex = result = getDexNative();
                }
            }
        }
        return result;
    }

    private native Dex getDexNative();
}

