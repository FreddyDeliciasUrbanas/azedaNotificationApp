package cl.azeda.www.azedaadministracion.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cl.azeda.www.azedaadministracion.Activities.MensajeActivity;
import cl.azeda.www.azedaadministracion.Models.Mensaje;
import cl.azeda.www.azedaadministracion.R;


public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajesViewHolder>{
    private List<Mensaje> mensajes;
    public MensajeAdapter(List<Mensaje> mensajes){
        this.mensajes = mensajes;
    }
    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mensajes_item, viewGroup, false);

        return new MensajesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MensajesViewHolder mensajesViewHolder, int i) {
        final Mensaje mensaje = mensajes.get(i);
        mensajesViewHolder.tvNombre.setText(mensaje.getNombreMensaje());
        mensajesViewHolder.tvDireccion.setText(mensaje.getDireccionMensaje());
        mensajesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MensajeActivity.class);
                int idMensajeEnv = mensaje.getIdMensaje();
                i.putExtra("id_mensaje", String.valueOf(idMensajeEnv));
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNombre;
        private TextView tvDireccion;

        public MensajesViewHolder(View itemView){
            super(itemView);
            tvNombre = (TextView)itemView.findViewById(R.id.tv_nombre_item);
            tvDireccion = (TextView)itemView.findViewById(R.id.tv_direccion_item);
        }
    }
}
