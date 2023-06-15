import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baicizhan.R
import com.example.baicizhan.databinding.ItemWordDetailBinding
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.player.Mp3Player

class WordDetailAdapter(private val wordResources: List<WordResource>) :
    RecyclerView.Adapter<WordDetailAdapter.WordResourceViewHolder>() {

    private lateinit var mp3Player: Mp3Player

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordResourceViewHolder {
        mp3Player = Mp3Player(parent.context)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWordDetailBinding.inflate(inflater, parent, false)
        return WordResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordResourceViewHolder, position: Int) {
        val currentWordResource = wordResources[position]
        holder.bind(currentWordResource)
    }

    override fun getItemCount() = wordResources.size

    inner class WordResourceViewHolder(private val binding: ItemWordDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val wordTextView: TextView = itemView.findViewById(R.id.word_detail_word)
        private val usphoneTextView: TextView = itemView.findViewById(R.id.word_detail_usphone)

        fun bind(wordResource: WordResource) {
            wordTextView.setOnClickListener{
                mp3Player.forcePlayMp3(Uri.parse(wordResource.usSpeechFile))
            }
            usphoneTextView.setOnClickListener{
                mp3Player.forcePlayMp3(Uri.parse(wordResource.usSpeechFile))
            }

            binding.wordResource = wordResource
            binding.executePendingBindings()
        }
    }
}
