{{/*
Create chart fullname
*/}}
{{- define "bookstore.fullname" -}}
{{- .Release.Name -}}
{{- end }}

{{/*
Common labels
*/}}
{{- define "bookstore.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}