package com.visioncameratextrecognition

class FrameMetadata private constructor(@JvmField val width: Int, @JvmField val height: Int, @JvmField val rotation: Int) {
    /** Builder of [FrameMetadata].  */
    class Builder {
        private var width = 0
        private var height = 0
        private var rotation = 0

        fun setWidth(width: Int): Builder {
            this.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            this.height = height
            return this
        }

        fun setRotation(rotation: Int): Builder {
            this.rotation = rotation
            return this
        }

        fun build(): FrameMetadata {
            return FrameMetadata(width, height, rotation)
        }
    }
}
